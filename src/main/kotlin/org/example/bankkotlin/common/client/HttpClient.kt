package org.example.bankkotlin.common.client

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class HttpClient(
    private val httpClient: OkHttpClient
) {

    fun GET(uri: String, headers: Map<String, String> = emptyMap()): String {
        val requestBuilder = Request.Builder().url(uri)
        headers.forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }
        val request = requestBuilder.build()

        return resultHandler(httpClient.newCall(request).execute())
    }

    fun POST(uri: String, headers: Map<String, String> = emptyMap(), body: RequestBody): String {
        val requestBuilder = Request.Builder().url(uri).post(body)
        headers.forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }
        val request = requestBuilder.build()

        return resultHandler(httpClient.newCall(request).execute())
    }

    private fun resultHandler(response: Response): String {
        response.use {
            if (!response.isSuccessful) {
                val message = "Http ${it.code}: ${it.body?.string() ?: "Unknown error"}"
                throw CustomException(ErrorCode.FAILED_TO_CALL_CLIENT, message)
            }
            return response.body?.string() ?: throw CustomException(ErrorCode.CALL_RESULT_BODY_NULL)
        }
    }
}