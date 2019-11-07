package dev.diegoliv.pontointeligente.services

import dev.diegoliv.pontointeligente.documents.funcionario.Lancamento
import dev.diegoliv.pontointeligente.repositories.interfaces.LancamentoRepository
import dev.diegoliv.pontointeligente.services.interfaces.ILancamentoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class LancamentoService(val _lancamentoRepository: LancamentoRepository): ILancamentoService {
    override fun buscarPorFuncionario(idFuncionario: String, pageRequest: PageRequest): Page<Lancamento> =
            _lancamentoRepository.findByIdFuncionario(idFuncionario, pageRequest)

    override fun buscarPorId(id: String): Lancamento? = _lancamentoRepository.findOne(id)

    override fun salvar(lancamento: Lancamento): Lancamento = _lancamentoRepository.save(lancamento)

    override fun remover(id: String) = _lancamentoRepository.delete(id)
}