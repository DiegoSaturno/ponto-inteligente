package dev.diegoliv.pontointeligente.infrastructure.security

import dev.diegoliv.pontointeligente.documents.funcionario.Funcionario
import dev.diegoliv.pontointeligente.services.FuncionarioService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class FuncionarioDetailsService(val _funcionarioService: FuncionarioService): UserDetailsService {
	
	override fun loadUserByUsername(username: String?): UserDetails {
		if (username != null) {
			val funcionario: Funcionario? = _funcionarioService.buscarPorEmail(username)
			
			if (funcionario != null)
				return FuncionarioPrincipal(funcionario)
		}
		
		throw UsernameNotFoundException("Usuário $username não encontrado.")
	}
}