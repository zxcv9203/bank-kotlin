package org.example.bankkotlin.common.exception

enum class ErrorCode(
    val code: Int,
    val message: String,
) {
    AUTH_CONFIG_NOT_FOUND(-100, "auth config not found")
}