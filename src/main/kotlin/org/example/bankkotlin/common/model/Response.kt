package org.example.bankkotlin.common.model

import org.springframework.http.HttpStatus

data class Response<T>(
    val code: Int,
    val message: String,
    val result: T?
) {

    companion object {
        fun <T> success(data: T): Response<T> {
            return Response(HttpStatus.OK.value(), "SUCCESS", data)
        }

        fun <T> failed(
            code: HttpStatus,
            message: String,
            data: T? = null
        ): Response<T> {
            return Response(code.value(), message, data)
        }
    }
}