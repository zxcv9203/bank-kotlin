package org.example.cosumer.exception

class CustomException(
    private val errorCode: ErrorCode,
    private val additionalMessage: String? = null,
) : RuntimeException(
    if (additionalMessage != null) {
        "${errorCode.message} - $additionalMessage"
    } else {
        errorCode.message
    }
)