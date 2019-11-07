package dev.diegoliv.pontointeligente.services

import dev.diegoliv.pontointeligente.documents.funcionario.Funcionario
import dev.diegoliv.pontointeligente.repositories.interfaces.FuncionarioRepository
import dev.diegoliv.pontointeligente.services.interfaces.IFuncionarioService
import org.springframework.stereotype.Service

@Service
class FuncionarioService(val _funcionarioRepository: FuncionarioRepository): IFuncionarioService {
    override fun buscarPorCpf(cpf: String): Funcionario? = _funcionarioRepository.findByCpf(cpf)
    override fun buscarPorEmail(email: String): Funcionario? = _funcionarioRepository.findByEmail(email)
    override fun buscarPorId(id: String): Funcionario? = _funcionarioRepository.findOne(id)
    override fun salvar(funcionario: Funcionario): Funcionario = _funcionarioRepository.save(funcionario)
}