package dev.diegoliv.pontointeligente.controllers

import dev.diegoliv.pontointeligente.controllers.dtos.EmpresaDto
import dev.diegoliv.pontointeligente.controllers.dtos.PessoaJuridicaDto
import dev.diegoliv.pontointeligente.controllers.response.Response
import dev.diegoliv.pontointeligente.documents.empresa.Empresa
import dev.diegoliv.pontointeligente.documents.funcionario.Funcionario
import dev.diegoliv.pontointeligente.documents.funcionario.enums.Perfil
import dev.diegoliv.pontointeligente.infrastructure.security.Password
import dev.diegoliv.pontointeligente.services.EmpresaService
import dev.diegoliv.pontointeligente.services.FuncionarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/empresa")
class EmpresaController(val _empresaService: EmpresaService): BaseController() {
	
	@GetMapping("/cnpj/{cnpj")
	fun buscarPorCnpj(@PathVariable cnpj: String): ResponseEntity<Response<EmpresaDto>> {
		val response: Response<EmpresaDto> = Response()
		
		val empresa = _empresaService.buscarPorCnpj(cnpj)
		
		response.data = if (empresa != null) EmpresaDto(empresa.razaoSocial, empresa.cnpj, empresa.id) else EmpresaDto()
		
		return ResponseEntity.ok(response)
	}
}