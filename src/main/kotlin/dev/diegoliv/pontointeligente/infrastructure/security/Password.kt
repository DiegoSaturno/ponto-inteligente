package dev.diegoliv.pontointeligente.infrastructure.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class Password {
    fun hashPassword(password: String): String = BCryptPasswordEncoder().encode(password)
}