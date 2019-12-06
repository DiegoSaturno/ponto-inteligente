package dev.diegoliv.pontointeligente.services

import dev.diegoliv.pontointeligente.documents.empresa.Empresa
import dev.diegoliv.pontointeligente.repositories.interfaces.EmpresaRepository
import dev.diegoliv.pontointeligente.services.interfaces.IEmpresaService
import org.springframework.stereotype.Service

@Service
class EmpresaService(val _empresaRepository: EmpresaRepository): IEmpresaService {
    override fun buscarPorCnpj(cnpj: String): Empresa? = _empresaRepository.findByCnpj(cnpj)
    override fun salvar(empresa: Empresa): Empresa = _empresaRepository.save(empresa)
}