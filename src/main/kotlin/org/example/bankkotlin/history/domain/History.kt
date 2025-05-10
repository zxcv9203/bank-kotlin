package org.example.bankkotlin.history.domain

import kotlinx.serialization.Serializable
import org.example.bankkotlin.common.util.BigDecimalSerializer
import org.example.bankkotlin.common.util.LocalDateTimeSerializer
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
class History(
    val fromUlid: String,
    val fromUser: String,

    val toUlid: String,
    val toUser: String,

    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @Serializable(with = LocalDateTimeSerializer::class)
    val time: LocalDateTime,
)