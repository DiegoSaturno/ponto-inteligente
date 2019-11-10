package dev.diegoliv.pontointeligente.controllers.dtos

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.NotEmpty

data class FuncionarioDto(
    @get:NotEmpty(message = "Nome não pode ser vazio")
    @get:Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
    val nome: String = "",

    @get:NotEmpty(message = "O Email deve ser preenchido.")
    @get:Length(min = 5, max = 200, message = "Email deve conter mais de 5 caracteres.")
    @get:Email(message = "Formato do email é inválido.")
    val email: String = "",
    val senha: String = "",

    val valorHora: String? = null,
    val cargaHoraria: String? = null,
    val quantidadeHorasAlmoco: String? = null,
    val id: String? = null
)