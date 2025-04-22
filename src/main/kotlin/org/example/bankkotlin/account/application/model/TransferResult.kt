package org.example.bankkotlin.account.application.model

import java.math.BigDecimal

data class TransferResult(
    val fromAccountBalance: BigDecimal,
    val toAccountBalance: BigDecimal,
)
