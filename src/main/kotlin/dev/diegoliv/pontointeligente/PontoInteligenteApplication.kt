package dev.diegoliv.pontointeligente

import dev.diegoliv.pontointeligente.documents.empresa.Empresa
import dev.diegoliv.pontointeligente.documents.funcionario.Funcionario
import dev.diegoliv.pontointeligente.documents.funcionario.enums.Perfil
import dev.diegoliv.pontointeligente.infrastructure.security.Password
import dev.diegoliv.pontointeligente.repositories.interfaces.EmpresaRepository
import dev.diegoliv.pontointeligente.repositories.interfaces.FuncionarioRepository
import dev.diegoliv.pontointeligente.repositories.interfaces.LancamentoRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class PontoInteligenteApplication(val _empresaRepository: EmpresaRepository, val _funcionarioRepository: FuncionarioRepository, val _lancamentoRepository: LancamentoRepository) : CommandLineRunner {
	override fun run(vararg args: String?) {
		_empresaRepository.deleteAll()
		_funcionarioRepository.deleteAll()
		_lancamentoRepository.deleteAll()

		val empresa: Empresa = Empresa("Empresa de Teste", "14234567801984")
		_empresaRepository.save(empresa)

		val admin: Funcionario = Funcionario("Admin", "admin@admin.com", Password().hashPassword("123456"), "12345678900", Perfil.ROLE_ADMIN, empresa.id!!)
		_funcionarioRepository.save(admin)

		val funcionario: Funcionario = Funcionario("Funcionario Comum", "fun@fun.com", Password().hashPassword("123456"), "15864782011", Perfil.ROLE_USUARIO, empresa.id)
		_funcionarioRepository.save(funcionario)

		println(empresa.toString())
		println(admin.toString())
	}
}

fun main(args: Array<String>) {
	SpringApplication.run(PontoInteligenteApplication::class.java, *args)
}
