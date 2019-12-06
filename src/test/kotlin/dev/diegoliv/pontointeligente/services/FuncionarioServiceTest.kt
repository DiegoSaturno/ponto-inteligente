package dev.diegoliv.pontointeligente.services

import dev.diegoliv.pontointeligente.documents.funcionario.Funcionario
import dev.diegoliv.pontointeligente.documents.funcionario.enums.Perfil
import dev.diegoliv.pontointeligente.infrastructure.security.Password
import dev.diegoliv.pontointeligente.repositories.interfaces.FuncionarioRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class FuncionarioServiceTest {
    @MockBean
    private val _funcionarioRepository: FuncionarioRepository? = null

    @Autowired
    private val _funcionarioService: FuncionarioService? = null

    @Autowired
    private lateinit var _password: Password

    private val email: String = "email@teste.com"
    private val cpf: String = "12345678900"
    private val id: String = "1"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito
            .given(_funcionarioRepository?.save(Mockito.any(Funcionario::class.java)))
            .willReturn(funcionario())

        BDDMockito
            .given(_funcionarioRepository?.findOne(id))
            .willReturn(funcionario())

        BDDMockito
            .given(_funcionarioRepository?.findByEmail(email))
            .willReturn(funcionario())

        BDDMockito
            .given(_funcionarioRepository?.findByCpf(cpf))
            .willReturn(funcionario())
    }

    @Test
    fun salvarFuncionario() {
        val funcionario: Funcionario? = this._funcionarioService?.salvar(funcionario())
        Assert.assertNotNull(funcionario)
    }

    @Test
    fun buscarPorCpf() {
        val funcionario: Funcionario? = _funcionarioService?.buscarPorCpf(cpf)
        Assert.assertNotNull(funcionario)
    }

    @Test
    fun buscarPorEmail() {
        val funcionario: Funcionario? = _funcionarioService?.buscarPorEmail(email)
        Assert.assertNotNull(funcionario)
    }

    @Test
    fun buscarPorId() {
        val funcionario: Funcionario? = _funcionarioService?.buscarPorId(id)
        Assert.assertNotNull(funcionario)
    }

    private fun funcionario(): Funcionario =
        Funcionario("Funcionário de Teste", email, _password.hashPassword("123456"), cpf, Perfil.ROLE_USUARIO, id)
}