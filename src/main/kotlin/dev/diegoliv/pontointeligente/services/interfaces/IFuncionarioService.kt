package dev.diegoliv.pontointeligente.services.interfaces

import dev.diegoliv.pontointeligente.documents.funcionario.Funcionario

interface IFuncionarioService {
    fun salvar(funcionario: Funcionario): Funcionario
    fun buscarPorCpf(cpf: String): Funcionario?
    fun buscarPorEmail(email: String): Funcionario?
    fun buscarPorId(id: String): Funcionario?
}