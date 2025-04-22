package org.example.bankkotlin.account.entrypoint

import org.example.bankkotlin.account.application.AccountTransactionUseCase
import org.example.bankkotlin.account.entrypoint.mapper.toDepositResponse
import org.example.bankkotlin.account.entrypoint.mapper.toResponse
import org.example.bankkotlin.account.entrypoint.request.DepositRequest
import org.example.bankkotlin.account.entrypoint.request.TransferRequest
import org.example.bankkotlin.account.entrypoint.response.DepositResponse
import org.example.bankkotlin.account.entrypoint.response.TransferResponse
import org.example.bankkotlin.common.model.Response
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/transactions")
class AccountTransactionController(
    private val accountTransactionUseCase: AccountTransactionUseCase
) {

    @PostMapping("/deposit")
    fun deposit(
        @RequestBody(required = true) request: DepositRequest,
    ): Response<DepositResponse> {
        return accountTransactionUseCase.deposit(request.toUlid, request.toAccountId, request.amount)
            .let { Response.success(it.toDepositResponse()) }
    }

    @PostMapping("/transfer")
    fun transfer(
        @RequestBody(required = true) request: TransferRequest,
    ): Response<TransferResponse> {
        return accountTransactionUseCase.transfer(
            request.fromUlid,
            request.fromAccountId,
            request.toAccountId,
            request.amount
        )
            .let { Response.success(it.toResponse()) }
    }
}