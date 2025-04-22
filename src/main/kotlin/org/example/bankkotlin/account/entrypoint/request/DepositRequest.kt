package org.example.bankkotlin.account.entrypoint.request

import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class DepositRequest(
    @field:NotBlank(message = "enter to account id")
    val toAccountId: String,

    @field:NotBlank(message = "enter to account id")
    val toUlid: String,

    @field:NotBlank(message = "enter amount")
    val amount: BigDecimal,
)
