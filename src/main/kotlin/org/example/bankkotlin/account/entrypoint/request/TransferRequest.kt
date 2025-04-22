package org.example.bankkotlin.account.entrypoint.request

import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class TransferRequest(
    @field:NotBlank(message = "enter from account id")
    val fromAccountId: String,
    @field:NotBlank(message = "enter to account id")
    val toAccountId: String,

    @field:NotBlank(message = "enter from ulid")
    val fromUlid: String,
    @field:NotBlank(message = "enter amount")
    val amount: BigDecimal,
) {
}