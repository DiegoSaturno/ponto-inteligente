package dev.diegoliv.pontointeligente.services

import dev.diegoliv.pontointeligente.documents.funcionario.Lancamento
import dev.diegoliv.pontointeligente.documents.funcionario.enums.TipoLancamento
import dev.diegoliv.pontointeligente.repositories.interfaces.LancamentoRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class LancamentoServiceTest {
    @Autowired
    private val _lancamentoService: LancamentoService? = null

    @MockBean
    private val _lancamentoRepository: LancamentoRepository? = null

    private val id: String = "1"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito
            .given<Page<Lancamento>>(_lancamentoRepository?.findByIdFuncionario(id, PageRequest(0, 10)))
            .willReturn(PageImpl(ArrayList<Lancamento>()))

        BDDMockito
            .given(_lancamentoRepository?.findOne(id))
            .willReturn(lancamento())

        BDDMockito
            .given(_lancamentoRepository?.save(BDDMockito.any(Lancamento::class.java)))
            .willReturn(lancamento())
    }

    @Test
    fun buscarPorFuncionario() {
        val lancamento: Page<Lancamento>? = _lancamentoService?.buscarPorFuncionario(id, PageRequest(0, 10))
        Assert.assertNotNull(lancamento)
    }

    @Test
    fun buscarPorId() {
        val lancamento: Lancamento? = _lancamentoService?.buscarPorId(id)
        Assert.assertNotNull(lancamento)
    }

    @Test
    fun salvarLancamento() {
        val lancamento: Lancamento? = _lancamentoService?.salvar(lancamento())
        Assert.assertNotNull(lancamento)
        Assert.assertEquals(lancamento(), lancamento)
    }

    private fun lancamento(): Lancamento = Lancamento(Date(), TipoLancamento.INICIO_TRABALHO, id)
}