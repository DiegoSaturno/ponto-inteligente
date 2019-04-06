package dev.diegoliv.pontointeligente.documents.funcionario

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Funcionario(val nome: String,
                       val email: String,
                       val senha: String,
                       val cpf: String,
                       val perfil: Perfil,
                       val idEmpresa: String,
                       val valorHora: Double? = 0.0,
                       val horasTrabalhoDia: Float? = 0.00f,
                       val horasAlmoco: Float? = 0.00f,
                       @Id val id: String? = null
)