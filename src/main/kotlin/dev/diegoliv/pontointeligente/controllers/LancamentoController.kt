package dev.diegoliv.pontointeligente.controllers

import dev.diegoliv.pontointeligente.controllers.dtos.LancamentoDto
import dev.diegoliv.pontointeligente.controllers.response.Response
import dev.diegoliv.pontointeligente.documents.funcionario.Funcionario
import dev.diegoliv.pontointeligente.documents.funcionario.Lancamento
import dev.diegoliv.pontointeligente.documents.funcionario.enums.TipoLancamento
import dev.diegoliv.pontointeligente.services.FuncionarioService
import dev.diegoliv.pontointeligente.services.LancamentoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import javax.validation.Valid

@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController(val _lancamentoService: LancamentoService, val _funcionarioService: FuncionarioService) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Value("\${application.pagination.itemsperpage }")
    val itemsPerPage: Int = 15;

    @PostMapping
    fun adicionar(@Valid @RequestBody lancamentoDto: LancamentoDto, result: BindingResult): ResponseEntity<Response<LancamentoDto>>  {
        val response: Response<LancamentoDto> = Response<LancamentoDto>()
        validarFuncionario(lancamentoDto, result)

        if (result.hasErrors()) {
            for (error in result.allErrors)
                response.errors.add(error.defaultMessage)

            return ResponseEntity.badRequest().body(response)
        }

        val lancamento = _lancamentoService.salvar(converterDtoParaLancamento(lancamentoDto))
        response.data = converterLancamentoParaDto(lancamento)

        return ResponseEntity.ok(response)
    }

    private fun validarFuncionario(lancamentoDto: LancamentoDto, result: BindingResult) {
        if (lancamentoDto.idFuncionario != null) {
            val funcionario: Funcionario? = _funcionarioService.buscarPorId(lancamentoDto.idFuncionario)

            if (funcionario == null) {
                result.addError(ObjectError("funcionario", "Funcionário não encontrado."))
            }
        } else {
            result.addError(ObjectError("funcionario", "Funcionário não informado."))
        }
    }

    private fun converterDtoParaLancamento(lancamentoDto: LancamentoDto): Lancamento {
        return Lancamento(dateFormat.parse(lancamentoDto.data), TipoLancamento.valueOf(lancamentoDto.tipo!!), lancamentoDto.idFuncionario!!,
                lancamentoDto.descricao, lancamentoDto.localizacao, lancamentoDto.id)
    }

    private fun converterLancamentoParaDto(lancamento: Lancamento): LancamentoDto =
            LancamentoDto(dateFormat.format(lancamento.data), lancamento.tipoLancamento.toString(), lancamento.descricao, lancamento.localizacao, lancamento.idFuncionario, lancamento.id)
}