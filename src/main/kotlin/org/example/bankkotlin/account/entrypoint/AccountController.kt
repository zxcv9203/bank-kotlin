package org.example.bankkotlin.account.entrypoint

import org.example.bankkotlin.account.application.AccountUseCase
import org.example.bankkotlin.common.model.Response
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1")
class AccountController(
    private val accountUseCase: AccountUseCase,
) {
    @PostMapping("/users/{userUlid}/accounts")
    fun create(
        @PathVariable("userUlid", required = true) accountId: String,
    ): Response<String> {
        return accountUseCase.create(accountId)
            .let { Response.success(it) }
    }

    @GetMapping("/users/{userUlid}/accounts/{accountUlid}/balance")
    fun balance(
        @PathVariable("userUlid", required = true) userId: String,
        @PathVariable("accountUlid", required = true) accountId: String,
    ): Response<BigDecimal> {
        return accountUseCase.balance(userId, accountId)
            .let { Response.success(it) }
    }

    @DeleteMapping("/users/{userUlid}/accounts/{accountUlid}")
    fun remove(
        @PathVariable("userUlid", required = true) userId: String,
        @PathVariable("accountUlid", required = true) accountId: String,
    ): Response<String> {
        return accountUseCase.remove(userId, accountId)
            .let { Response.success(it) }
    }
}