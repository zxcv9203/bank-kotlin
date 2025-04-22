package org.example.bankkotlin.account.entrypoint.mapper

import org.example.bankkotlin.account.application.model.TransferResult
import org.example.bankkotlin.account.domain.Account
import org.example.bankkotlin.account.entrypoint.response.DepositResponse
import org.example.bankkotlin.account.entrypoint.response.TransferResponse

fun TransferResult.toResponse(): TransferResponse {
    return TransferResponse(
        fromAccountBalance = this.fromAccountBalance.toString(),
        toAccountBalance = this.toAccountBalance.toString()
    )
}

fun Account.toDepositResponse(): DepositResponse {
    return DepositResponse(
        balance = this.balance.toString()
    )
}