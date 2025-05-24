package org.example.cosumer.types.entity

import kotlinx.serialization.Serializable
import org.example.cosumer.common.json.BigDecimalSerializer
import org.example.cosumer.common.json.LocalDateTimeSerializer
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