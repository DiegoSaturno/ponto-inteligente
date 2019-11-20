package dev.diegoliv.pontointeligente.controllers

import dev.diegoliv.pontointeligente.controllers.response.Response
import org.springframework.beans.factory.annotation.Value
import java.text.SimpleDateFormat

open class BaseController {
    @Value("\${application.pagination.itemsperpage}")
    val itemsPerPage: Int = 15;

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
}