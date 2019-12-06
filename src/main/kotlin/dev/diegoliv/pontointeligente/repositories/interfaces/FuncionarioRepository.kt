package dev.diegoliv.pontointeligente.repositories.interfaces

import dev.diegoliv.pontointeligente.documents.funcionario.Funcionario
import org.springframework.data.mongodb.repository.MongoRepository

interface FuncionarioRepository: MongoRepository<Funcionario, String> {
    fun findByEmail(email: String): Funcionario
    fun findByCpf(email: String): Funcionario
}