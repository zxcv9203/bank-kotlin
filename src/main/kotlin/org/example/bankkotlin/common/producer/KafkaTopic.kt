package org.example.bankkotlin.common.producer

enum class KafkaTopic(
    val topic: String,
) {
    TRANSACTION("transaction"),
}