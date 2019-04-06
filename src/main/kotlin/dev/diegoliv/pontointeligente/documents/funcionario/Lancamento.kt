package dev.diegoliv.pontointeligente.documents.funcionario

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Lancamento(val data: Date,
                      val tipo: Tipo,
                      val idFuncionario: String,
                      val descricao: String? = "",
                      val localizacao: String? = "",
                      @Id val id: String? = null
)