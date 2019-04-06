package dev.diegoliv.pontointeligente.services

import dev.diegoliv.pontointeligente.documents.empresa.Empresa
import dev.diegoliv.pontointeligente.repositories.interfaces.EmpresaRepository
import dev.diegoliv.pontointeligente.services.interfaces.IEmpresaService
import org.springframework.stereotype.Service

@Service
class EmpresaService(val empresaRepository: EmpresaRepository): IEmpresaService {
    override fun buscarPorCnpj(cnpj: String): Empresa? = empresaRepository.findByCnpj(cnpj)

    override fun salvar(empresa: Empresa): Empresa = empresaRepository.save(empresa)
}