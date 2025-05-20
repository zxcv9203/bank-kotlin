package org.example.bankkotlin.history.entrypoint

import org.example.bankkotlin.common.model.Response
import org.example.bankkotlin.history.domain.History
import org.example.bankkotlin.history.domain.HistoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/histories")
class HistoryController(
    private val historyService: HistoryService
) {
    @GetMapping("/{ulid}")
    fun history(@PathVariable(required = true) ulid: String): Response<List<History>> {
        return Response.success(historyService.history(ulid))
    }
}