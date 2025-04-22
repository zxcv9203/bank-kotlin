package org.example.bankkotlin.account.entrypoint.response

data class TransferResponse(
    val fromAccountBalance: String,
    val toAccountBalance: String,
)
