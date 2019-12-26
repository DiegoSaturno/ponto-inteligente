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
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import javax.validation.Valid

@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController(val _lancamentoService: LancamentoService, val _funcionarioService: FuncionarioService): BaseController() {

	@PostMapping
	fun adicionar(@Valid @RequestBody lancamentoDto: LancamentoDto, result: BindingResult): ResponseEntity<Response<LancamentoDto>>  {
		return salvarLancamento(lancamentoDto, result)
	}

	@GetMapping(value = ["/{id}"])
	fun obterPorId(@PathVariable("id") id: String): ResponseEntity<Response<LancamentoDto>> {
		val response: Response<LancamentoDto> = Response()

		val lancamento: Lancamento? = _lancamentoService.buscarPorId(id)

		response.data = if (lancamento != null) converterLancamentoParaDto(lancamento) else LancamentoDto()
		return ResponseEntity.ok(response)
	}

	@PutMapping(value = ["/{id}"])
	fun atualizar(@PathVariable id: String, @Valid @RequestBody lancamentoDto: LancamentoDto, result: BindingResult): ResponseEntity<Response<LancamentoDto>> {
		lancamentoDto.id = id;

		return salvarLancamento(lancamentoDto, result)
	}

	@DeleteMapping(value = ["/{id}"])
	@PreAuthorize("hasAnyRole('ADMIN')")
	fun remover(@PathVariable id: String): ResponseEntity<Response<String>> {
		val response: Response<String> = Response()
		val lancamento = _lancamentoService.buscarPorId(id)

		if (lancamento == null) {
			response.errors.add("Lançamento não encontrado.")

			return ResponseEntity.badRequest().body(response)
		}

		_lancamentoService.remover(id)
		return ResponseEntity.ok(response)
	}

	private fun salvarLancamento(lancamentoDto: LancamentoDto, result: BindingResult): ResponseEntity<Response<LancamentoDto>> {
		val response: Response<LancamentoDto> = Response()
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
		return Lancamento(dateFormat.parse(lancamentoDto.data), TipoLancamento.valueOf(lancamentoDto.tipo!!), lancamentoDto.idFuncionario!!, lancamentoDto.descricao, lancamentoDto.localizacao, lancamentoDto.id)
	}

	private fun converterLancamentoParaDto(lancamento: Lancamento): LancamentoDto =
		LancamentoDto(dateFormat.format(lancamento.data), lancamento.tipoLancamento.toString(), lancamento.descricao, lancamento.localizacao, lancamento.idFuncionario, lancamento.id)
}