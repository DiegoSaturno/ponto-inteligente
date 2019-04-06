package dev.diegoliv.pontointeligente.infrastructure.security

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class Password {
    fun hashPassword(password: String): String = BCryptPasswordEncoder().encode(password)
}