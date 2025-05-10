package org.example.bankkotlin.history.repository

import org.example.bankkotlin.history.domain.History

fun TransactionHistoryDocument.toResponse(
    fromUser: String,
    toUser: String,
) = History(
    fromUlid = this.fromUlid,
    fromUser = fromUser,

    toUlid = this.toUlid,
    toUser = toUser,

    amount = this.amount,
    time = this.time,
)
