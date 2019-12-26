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
class PontoInteligenteApplication {
	fun main(args: Array<String>) {
		SpringApplication.run(PontoInteligenteApplication::class.java, *args)
	}
}