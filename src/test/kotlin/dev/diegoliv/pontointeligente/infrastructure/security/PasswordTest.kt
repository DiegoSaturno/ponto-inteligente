package dev.diegoliv.pontointeligente.infrastructure.security

import org.junit.Assert
import org.junit.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class PasswordTest {
    private val _senha = "abcdef"
    private val _bcryptEncoder = BCryptPasswordEncoder()

    @Test
    fun gerarHashSenha() {
        val hash = Password().hashPassword(_senha)
        Assert.assertTrue(_bcryptEncoder.matches(_senha, hash))
    }
}