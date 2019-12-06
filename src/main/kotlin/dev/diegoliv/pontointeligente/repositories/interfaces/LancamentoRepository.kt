package dev.diegoliv.pontointeligente.repositories.interfaces

import dev.diegoliv.pontointeligente.documents.funcionario.Lancamento
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository


interface LancamentoRepository: MongoRepository<Lancamento, String> {
    fun findByIdFuncionario(idFuncionario: String, pageable: Pageable): Page<Lancamento>
}