package org.example.cosumer.types.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.cosumer.common.json.BigDecimalSerializer
import org.example.cosumer.common.json.LocalDateTimeSerializer
import org.example.cosumer.types.entity.History
import org.example.cosumer.types.message.TransactionHistoryDocument
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
) {
    fun toEntity(): TransactionHistoryDocument {
        return TransactionHistoryDocument(
            fromUlid = fromUlid,
            toUlid = toUlid,
            amount = amount,
            time = time,
        )
    }

    fun toDto(): History {
        return History(
            fromUlid = fromUlid,
            fromUser = fromName,
            toUlid = toUlid,
            toUser = toName,
            amount = amount,
            time = time,
        )
    }
}