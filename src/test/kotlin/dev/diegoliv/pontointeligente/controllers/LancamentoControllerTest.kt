package dev.diegoliv.pontointeligente.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import dev.diegoliv.pontointeligente.controllers.dtos.LancamentoDto
import dev.diegoliv.pontointeligente.documents.funcionario.Funcionario
import dev.diegoliv.pontointeligente.documents.funcionario.Lancamento
import dev.diegoliv.pontointeligente.documents.funcionario.enums.Perfil
import dev.diegoliv.pontointeligente.documents.funcionario.enums.TipoLancamento
import dev.diegoliv.pontointeligente.infrastructure.security.Password
import dev.diegoliv.pontointeligente.services.FuncionarioService
import dev.diegoliv.pontointeligente.services.LancamentoService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class LancamentoControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @MockBean
    private val _lancamentoService: LancamentoService? = null

    @MockBean
    private val _funcionarioService: FuncionarioService? = null

    private val urlBase: String = "/api/lancamentos"
    private val idFuncionario: String = "1"
    private val idLancamento: String = "1"
    private val tipo: String = TipoLancamento.INICIO_TRABALHO.name
    private val data: Date = Date()

    private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    private val lancamento: Lancamento = Lancamento(data, TipoLancamento.valueOf(tipo), idFuncionario, "Descrição", "1.243,3.567", idLancamento)
    private val lancamentoDto: LancamentoDto = LancamentoDto(dateFormat.format(data), TipoLancamento.INICIO_TRABALHO.toString(), idFuncionario, "Descrição", "1.243,3.567", idLancamento)
    private val lancamentoDtoSemId: LancamentoDto = LancamentoDto(dateFormat.format(data), TipoLancamento.INICIO_TRABALHO.toString(), idFuncionario, "Descrição", "1.243,3.567")


    @Test
    @Throws(Exception::class)
    @WithMockUser
    fun cadastrarLancamento() {
        BDDMockito
            .given<Funcionario>(_funcionarioService?.buscarPorId(idFuncionario))
            .willReturn(funcionario())

        BDDMockito
            .given<Lancamento>(_lancamentoService?.salvar(lancamento))
            .willReturn(lancamento)

        mvc!!
            .perform(
                MockMvcRequestBuilders.post(urlBase)
                    .content(obterJsonRequest())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.tipo").value(tipo))
                .andExpect(jsonPath("$.data.data").value(dateFormat.format(data)))
                .andExpect(jsonPath("$.data.idFuncionario").value(idFuncionario))
                .andExpect(jsonPath("$.errors").isEmpty)
    }

    private fun obterJsonRequest(): String = ObjectMapper().writeValueAsString(lancamentoDtoSemId)
    private fun funcionario(): Funcionario = Funcionario("Nome", "email@email.com", Password().hashPassword("1234"), "12398745688", Perfil.ROLE_USUARIO, idFuncionario)
}