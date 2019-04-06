package dev.diegoliv.pontointeligente.services.interfaces

import dev.diegoliv.pontointeligente.documents.empresa.Empresa

interface IEmpresaService {
    fun buscarPorCnpj(cnpj: String): Empresa?
    fun salvar(empresa: Empresa): Empresa
}