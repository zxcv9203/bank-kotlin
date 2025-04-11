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
    FAILED_TO_INVOKE_TO_LOGGER(-106, "failed to invoke to logger"),
    USER_NOT_FOUND(-107, "user not found"),
    FAILED_TO_SAVE_DATA(-108, "failed to save data"),
    ACCOUNT_NOT_FOUND(-109, "account not found"),
    MISS_MATCH_ACCOUNT_ULID_AND_USER_ULID(-110, "miss match account ulid and user ulid"),
    ACCOUNT_BALANCE_IS_NOT_ZERO(-111, "account balance is not zero"),
    FAILED_TO_MUTEX_INVOKE(-112, "failed to mutex invoke"),
}