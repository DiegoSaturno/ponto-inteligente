package dev.diegoliv.pontointeligente.controllers

import dev.diegoliv.pontointeligente.controllers.dtos.EmpresaDto
import dev.diegoliv.pontointeligente.controllers.dtos.PessoaJuridicaDto
import dev.diegoliv.pontointeligente.controllers.response.Response
import dev.diegoliv.pontointeligente.documents.empresa.Empresa
import dev.diegoliv.pontointeligente.documents.funcionario.Funcionario
import dev.diegoliv.pontointeligente.documents.funcionario.enums.Perfil
import dev.diegoliv.pontointeligente.infrastructure.security.Password
import dev.diegoliv.pontointeligente.services.EmpresaService
import dev.diegoliv.pontointeligente.services.FuncionarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/empresa")
class EmpresaController(val _empresaService: EmpresaService, val _funcionarioService: FuncionarioService): BaseController() {

	@Autowired
	private lateinit var _password: Password

	@PostMapping
	private fun adicionar(@Valid @RequestBody pessoaJuridicaDto: PessoaJuridicaDto, result: BindingResult): ResponseEntity<Response<PessoaJuridicaDto>> {
		val response: Response<PessoaJuridicaDto> = Response<PessoaJuridicaDto>()

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

	private fun validarDadosPJ(pessoaJuridicaDto: PessoaJuridicaDto, result: BindingResult) {
		val empresa: Empresa? = _empresaService.buscarPorCnpj(pessoaJuridicaDto.cnpj)

		if (empresa != null) {
			result.addError(ObjectError("empresa", "Empresa já existe."))
		}

		val funcionario: Funcionario? = _funcionarioService.buscarPorCpf(pessoaJuridicaDto.cpf)

		if (empresa != null) {
			result.addError(ObjectError("funcionario", "Funcionário responsável já existe."))
		}
	}
}