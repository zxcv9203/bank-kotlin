package org.example.bankkotlin.auth.infrastructure.web

import okhttp3.FormBody
import org.example.bankkotlin.auth.domain.OAuthClient
import org.example.bankkotlin.auth.infrastructure.web.config.OAuth2Config
import org.example.bankkotlin.auth.infrastructure.web.response.GoogleOAuth2TokenResponse
import org.example.bankkotlin.auth.infrastructure.web.response.GoogleOAuth2UserResponse
import org.example.bankkotlin.auth.infrastructure.web.response.OAuth2TokenResponse
import org.example.bankkotlin.auth.infrastructure.web.response.OAuth2UserResponse
import org.example.bankkotlin.common.client.HttpClient
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.example.bankkotlin.common.util.JsonUtils
import org.springframework.stereotype.Component

@Component(GoogleOAuthClient.CLIENT_ID)
class GoogleOAuthClient(
    private val config: OAuth2Config,
    private val httpClient: HttpClient,
) : OAuthClient {

    private val oAuthInfo = config.providers[CLIENT_ID]
        ?: throw CustomException(ErrorCode.AUTH_CONFIG_NOT_FOUND, CLIENT_ID)
    override val providerName: String
        get() = CLIENT_ID

    override fun getToken(code: String): OAuth2TokenResponse {
        val body = FormBody.Builder()
            .add("code", code)
            .add("client_id", oAuthInfo.clientId)
            .add("client_secret", oAuthInfo.clientSecret)
            .add("redirect_uri", oAuthInfo.redirectUri)
            .add("grant_type", "authorization_code")
            .build()

        val headers = mapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/x-www-form-urlencoded"
        )

        val jsonString = httpClient.POST(TOKEN_URL, headers, body)

        val response = JsonUtils.decodeFromJson(jsonString, GoogleOAuth2TokenResponse.serializer())

        return response
    }

    override fun getUserInfo(accessToken: String): OAuth2UserResponse {
        val headers = mapOf(
            "Content-Type" to "application/json",
            "Authorization" to "Bearer $accessToken",
        )

        val jsonString = httpClient.GET(USER_INFO_URL, headers)
        val response = JsonUtils.decodeFromJson(jsonString, GoogleOAuth2UserResponse.serializer())
        return response
    }

    companion object {
        const val CLIENT_ID = "google"
        const val TOKEN_URL = "https://oauth2.googleapis.com/token"
        const val USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo"
    }
}