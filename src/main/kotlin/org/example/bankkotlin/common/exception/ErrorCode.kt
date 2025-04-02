package org.example.bankkotlin.common.exception

enum class ErrorCode(
    val code: Int,
    val message: String,
) {
    AUTH_CONFIG_NOT_FOUND(-100, "auth config not found"),
    FAILED_TO_CALL_CLIENT(-101, "failed to call client"),
    CALL_RESULT_BODY_NULL(-102, "invalid oauth2 token"),
    PROVIDER_NOT_FOUND(-103, "provider not found"),
    TOKEN_IS_INVALID(-104, "token is invalid"),
    TOKEN_IS_EXPIRED(-105, "token is expired"),
}