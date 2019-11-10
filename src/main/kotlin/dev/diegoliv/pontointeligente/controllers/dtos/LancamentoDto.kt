package dev.diegoliv.pontointeligente.controllers.dtos

import org.hibernate.validator.constraints.NotEmpty

data class LancamentoDto(
    @get:NotEmpty(message = "O campo Data é obrigatório.")
    val data: String? = null,

    @get:NotEmpty(message = "O campo Tipo do Lançamento é obrigatório")
    val tipo: String? = null,

    val descricao: String? = null,
    val localizacao: String? = null,
    val idFuncionario: String? = null,
    val id: String? = null
)