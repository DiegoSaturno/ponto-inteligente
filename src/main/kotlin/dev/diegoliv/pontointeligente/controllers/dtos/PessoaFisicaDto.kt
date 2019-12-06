package dev.diegoliv.pontointeligente.controllers.dtos

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.NotEmpty
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF

data class PessoaFisicaDto(
    @get:NotEmpty(message = "O nome é obrigatório.")
    @get:Length(min = 3, message = "O campo nome deve ter mais de 3 caracteres.")
    val nome: String = "",

    @get:NotEmpty(message = "O Email deve ser preenchido.")
    @get:Length(min = 5, max = 200, message = "Email deve conter mais de 5 caracteres.")
    @get:Email(message = "Formato do email é inválido.")
    val email: String = "",

    @get:NotEmpty(message = "O campo Senha é obrigatório.")
    var senha: String = "",

    @get:NotEmpty(message = "O campo CPF é obrigatório.")
    @get:CPF(message = "O CPF informado é inválido.")
    val cpf: String = "",

    @get:NotEmpty(message = "O campo CNPJ é obrigatório")
    @get:CNPJ(message = "O CNPJ informado é inválido.")
    val cnpj: String = "",

    val idEmpresa: String = "",

    val valorHora: String? = null,
    val cargaHoraria: String? = null,
    val quantidadeHorasAlmoco: String? = null,
    val id: String? = null
)