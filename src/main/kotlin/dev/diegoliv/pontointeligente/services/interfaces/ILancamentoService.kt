package dev.diegoliv.pontointeligente.services.interfaces

import dev.diegoliv.pontointeligente.documents.funcionario.Lancamento
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface ILancamentoService {
    fun buscarPorFuncionario(idFuncionario: String, pageRequest: PageRequest): Page<Lancamento>
    fun buscarPorId(id: String): Lancamento?
    fun salvar(lancamento: Lancamento): Lancamento
    fun remover(id: String)
}