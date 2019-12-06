package dev.diegoliv.pontointeligente.repositories.interfaces

import dev.diegoliv.pontointeligente.documents.empresa.Empresa
import org.springframework.data.mongodb.repository.MongoRepository

interface EmpresaRepository : MongoRepository<Empresa, String> {
    fun findByCnpj(cnpj: String): Empresa
}