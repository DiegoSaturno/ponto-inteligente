package dev.diegoliv.pontointeligente.controllers

import dev.diegoliv.pontointeligente.controllers.dtos.FuncionarioDto
import dev.diegoliv.pontointeligente.controllers.dtos.LancamentoDto
import dev.diegoliv.pontointeligente.controllers.dtos.PessoaFisicaDto
import dev.diegoliv.pontointeligente.controllers.dtos.PessoaJuridicaDto
import dev.diegoliv.pontointeligente.controllers.response.Response
import dev.diegoliv.pontointeligente.documents.empresa.Empresa
import dev.diegoliv.pontointeligente.documents.funcionario.Funcionario
import dev.diegoliv.pontointeligente.documents.funcionario.Lancamento
import dev.diegoliv.pontointeligente.documents.funcionario.enums.Perfil
import dev.diegoliv.pontointeligente.infrastructure.security.Password
import dev.diegoliv.pontointeligente.services.EmpresaService
import dev.diegoliv.pontointeligente.services.FuncionarioService
import dev.diegoliv.pontointeligente.services.LancamentoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/funcionarios")
class FuncionarioController(val _funcionarioService: FuncionarioService, val _lancamentoService: LancamentoService, val _empresaService: EmpresaService, val _password: Password): BaseController() {
    
    @GetMapping("/{id}")
    fun getFuncionario(@PathVariable id: String): ResponseEntity<Response<FuncionarioDto>> {
        val response: Response<FuncionarioDto> = Response()
        
        val funcionario = _funcionarioService.buscarPorId(id)
        
        response.data = if (funcionario != null) converterFuncionaroParaFuncionarioDto(funcionario) else FuncionarioDto()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}/lancamentos")
    fun obterTodosLancamentos(@PathVariable id: String,
                              @RequestParam(value = "pag", defaultValue = "0") page: Int,
                              @RequestParam(value = "orderBy", defaultValue = "id") orderBy: String,
                              @RequestParam(value = "dir", defaultValue = "DESC") direction: String): ResponseEntity<Response<Page<LancamentoDto>>> {
        val response: Response<Page<LancamentoDto>> = Response<Page<LancamentoDto>>()

        val lancamentos: Page<Lancamento> = _lancamentoService.buscarPorFuncionario(id, PageRequest(page, itemsPerPage, Sort.Direction.valueOf(direction), orderBy))

        val lancamentosDto: Page<LancamentoDto> = lancamentos.map { LancamentoDto(dateFormat.format(it.data), it.tipoLancamento.toString(), it.descricao, it.localizacao, it.idFuncionario, it.id) }

        response.data = lancamentosDto
        return ResponseEntity.ok(response)
    }
    
    @PostMapping("/pj/")
    private fun adicionar(@Valid @RequestBody pessoaJuridicaDto: PessoaJuridicaDto, result: BindingResult): ResponseEntity<Response<PessoaJuridicaDto>> {
        val response: Response<PessoaJuridicaDto> = Response()
        
        validarDadosPJ(pessoaJuridicaDto, result)
        if (result.hasErrors()) {
            for (error in result.allErrors)
                response.errors.add(error.defaultMessage)
            
            return ResponseEntity.badRequest().body(response)
        }
        
        val empresa: Empresa = _empresaService.salvar(Empresa(pessoaJuridicaDto.razaoSocial, pessoaJuridicaDto.cnpj))
        val funcionario: Funcionario = _funcionarioService.salvar(Funcionario(pessoaJuridicaDto.nome, pessoaJuridicaDto.email, _password.hashPassword(pessoaJuridicaDto.senha), pessoaJuridicaDto.cpf, Perfil.ROLE_ADMIN, empresa.id!!))
        
        response.data = PessoaJuridicaDto(funcionario.nome, funcionario.email, "", funcionario.cpf, empresa.cnpj, empresa.razaoSocial)
        return ResponseEntity.ok(response)
    }
    
    @PostMapping("/pf/")
    fun adicionar(@Valid @RequestBody pessoaFisicaDto: PessoaFisicaDto,
                  result: BindingResult): ResponseEntity<Response<PessoaFisicaDto>> {
        val response: Response<PessoaFisicaDto> = Response()
        
        val empresa: Empresa? = _empresaService.buscarPorCnpj(pessoaFisicaDto.cnpj)
        validarEntrada(pessoaFisicaDto, result, empresa)
        
        if (result.hasErrors()) {
            for (error in result.allErrors)
                response.errors.add(error.defaultMessage)
            
            return ResponseEntity.badRequest().body(response)
        }
        
        val funcionario = _funcionarioService.salvar(converterPfDtoParaFuncionario(pessoaFisicaDto, empresa!!))
        response.data = converterFuncionarioParaPfDto(funcionario, empresa)
        
        return ResponseEntity.ok(response)
    }
    
    @PutMapping("/{id}")
    fun atualizarFuncionario(@PathVariable id: String, @Valid @RequestBody funcionarioDto: FuncionarioDto, result: BindingResult): ResponseEntity<Response<FuncionarioDto>> {
        
        val response: Response<FuncionarioDto> = Response()
        
        val funcionario = _funcionarioService.buscarPorId(id)
        
        if (funcionario == null) {
            addError(result, "funcionario", "Funcionário não existente.")
        }
        
        if (result.hasErrors()) {
            for (error in result.allErrors)
                response.errors.add(error.defaultMessage)
            
            return ResponseEntity.badRequest().body(response)
        }
        
        val funcionarioAtualizado = _funcionarioService.salvar(mapearCamposFuncionario(funcionario!!, funcionarioDto))
        response.data = converterFuncionaroParaFuncionarioDto(funcionarioAtualizado)
        
        return ResponseEntity.ok(response)
    }
    
    private fun validarDadosPJ(pessoaJuridicaDto: PessoaJuridicaDto, result: BindingResult) {
        val empresa: Empresa? = _empresaService.buscarPorCnpj(pessoaJuridicaDto.cnpj)
        
        if (empresa != null) {
            result.addError(ObjectError("empresa", "Empresa já existe."))
        }
        
        val funcionario: Funcionario? = _funcionarioService.buscarPorCpf(pessoaJuridicaDto.cpf)
        
        if (funcionario != null) {
            result.addError(ObjectError("funcionario", "Funcionário responsável já existe."))
        }
    }
    
    private fun validarEntrada(pessoaFisicaDto: PessoaFisicaDto, result: BindingResult, empresa: Empresa?) {
        if (empresa == null) {
            addError(result, "empresa", "Empresa não encontrada.")
        }
        
        val funcionarioPorCpf = _funcionarioService.buscarPorCpf(pessoaFisicaDto.cpf)
        val funcionarioPorEmail = _funcionarioService.buscarPorEmail(pessoaFisicaDto.email)
        if (funcionarioPorCpf != null) {
            addError(result, "funcionario", "CPF informado já existe.")
        }
    
        if (funcionarioPorEmail != null) {
            addError(result, "funcionario", "Email informado já existe.")
        }
    }
    
    private fun addError(result: BindingResult, tag: String, message: String) {
        result.addError(ObjectError(tag, message))
    }
    
    private fun mapearCamposFuncionario(funcionario: Funcionario, funcionarioDto: FuncionarioDto): Funcionario =
        Funcionario(
            funcionarioDto.nome,
            funcionario.email,
            if (funcionarioDto.senha != "") _password.hashPassword(funcionarioDto.senha) else funcionario.senha,
            funcionario.cpf,
            funcionario.perfil,
            funcionario.idEmpresa,
            funcionarioDto.valorHora?.toDouble(),
            funcionarioDto.cargaHoraria?.toFloat(),
            funcionarioDto.quantidadeHorasAlmoco?.toFloat(),
            funcionario.id)
    
    private fun converterFuncionaroParaFuncionarioDto(funcionario: Funcionario): FuncionarioDto = FuncionarioDto(funcionario.nome, funcionario.email, "", funcionario.valorHora.toString(), funcionario.horasTrabalhoDia.toString(), funcionario.horasAlmoco.toString(), funcionario.id.toString())
    
    private fun converterPfDtoParaFuncionario(pessoaFisicaDto: PessoaFisicaDto, empresa: Empresa) = Funcionario(pessoaFisicaDto.nome, pessoaFisicaDto.email, _password.hashPassword(pessoaFisicaDto.senha), pessoaFisicaDto.cpf, Perfil.ROLE_USUARIO, empresa.id.toString(), pessoaFisicaDto.valorHora?.toDouble(), pessoaFisicaDto.cargaHoraria?.toFloat(), pessoaFisicaDto.quantidadeHorasAlmoco?.toFloat(), pessoaFisicaDto.id)
    
    private fun converterFuncionarioParaPfDto(funcionario: Funcionario, empresa: Empresa) = PessoaFisicaDto(funcionario.nome, funcionario.email, "", funcionario.cpf, empresa.cnpj, empresa.id.toString(), funcionario.valorHora.toString(), funcionario.horasTrabalhoDia.toString(), funcionario.horasAlmoco.toString(), funcionario.id.toString())
}