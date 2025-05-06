package org.example.bankkotlin.account.application.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.bankkotlin.common.util.BigDecimalSerializer
import org.example.bankkotlin.common.util.LocalDateTimeSerializer
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
data class TransactionMessage(
    @SerialName("fromUlid")
    val fromUlid: String,
    @SerialName("fromName")
    val fromName: String,
    @SerialName("fromAccountId")
    val fromAccountId: String,

    @SerialName("toUlid")
    val toUlid: String,
    @SerialName("toName")
    val toName: String,
    @SerialName("toAccountId")
    val toAccountId: String,

    @SerialName("amount")
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @SerialName("time")
    @Serializable(with = LocalDateTimeSerializer::class)
    val time: LocalDateTime = LocalDateTime.now(),
)