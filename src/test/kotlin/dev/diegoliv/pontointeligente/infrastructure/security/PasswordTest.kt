package dev.diegoliv.pontointeligente.infrastructure.security

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class PasswordTest {

    @Autowired
    private lateinit var _password: Password

    private val _senha = "abcdef"
    private val _bcryptEncoder = BCryptPasswordEncoder()

    @Test
    fun gerarHashSenha() {
        val hash = _password.hashPassword(_senha)
        Assert.assertTrue(_bcryptEncoder.matches(_senha, hash))
    }
}