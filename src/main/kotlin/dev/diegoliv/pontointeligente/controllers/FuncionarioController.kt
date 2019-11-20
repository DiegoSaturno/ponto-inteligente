package dev.diegoliv.pontointeligente.controllers

import dev.diegoliv.pontointeligente.controllers.dtos.LancamentoDto
import dev.diegoliv.pontointeligente.controllers.response.Response
import dev.diegoliv.pontointeligente.documents.funcionario.Lancamento
import dev.diegoliv.pontointeligente.services.FuncionarioService
import dev.diegoliv.pontointeligente.services.LancamentoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/funcionarios")
class FuncionarioController(val _funcionarioService: FuncionarioService, val _lancamentoService: LancamentoService): BaseController() {

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
}