package org.example.bankkotlin.history.repository

import kotlinx.serialization.Serializable
import org.example.bankkotlin.common.util.BigDecimalSerializer
import org.example.bankkotlin.common.util.LocalDateTimeSerializer
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
@Document(collection = "transaction_history")
class TransactionHistoryDocument(
    val fromUlid: String,
    val toUlid: String,

    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @Serializable(with = LocalDateTimeSerializer::class)
    val time: LocalDateTime,
)