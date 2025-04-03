package org.example.bankkotlin.auth.infrastructure.web

import okhttp3.FormBody
import org.example.bankkotlin.auth.domain.OAuthClient
import org.example.bankkotlin.auth.infrastructure.web.config.OAuth2Config
import org.example.bankkotlin.auth.infrastructure.web.response.GithubOAuth2TokenResponse
import org.example.bankkotlin.auth.infrastructure.web.response.GithubOAuth2UserClientResponse
import org.example.bankkotlin.auth.infrastructure.web.response.OAuth2TokenResponse
import org.example.bankkotlin.auth.infrastructure.web.response.OAuth2UserResponse
import org.example.bankkotlin.common.client.HttpClient
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.example.bankkotlin.common.util.JsonUtils
import org.springframework.stereotype.Component

@Component(GithubOAuthClient.CLIENT_ID)
class GithubOAuthClient(
    private val config: OAuth2Config,
    private val httpClient: HttpClient,
) : OAuthClient {

    private val oAuthInfo = config.providers[CLIENT_ID]
        ?: throw CustomException(ErrorCode.AUTH_CONFIG_NOT_FOUND, GoogleOAuthClient.CLIENT_ID)

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

        val headers = mapOf("Accept" to "application/json")

        val jsonString = httpClient.POST(TOKEN_URL, headers, body)

        val response = JsonUtils.decodeFromJson(jsonString, GithubOAuth2TokenResponse.serializer())

        return response
    }

    override fun getUserInfo(accessToken: String): OAuth2UserResponse {
        val headers = mapOf(
            "Content-Type" to "application/json",
            "Authorization" to "Bearer $accessToken",
        )

        val jsonString = httpClient.GET(TOKEN_URL, headers)
        val response = JsonUtils.decodeFromJson(jsonString, GithubOAuth2UserClientResponse.serializer())
        return response.toOAuth2UserResponse()
    }

    companion object {
        const val CLIENT_ID = "github"
        const val TOKEN_URL = "https://github.com/login/oauth/access_token"
        const val USER_INFO_URL = "https://api.github.com/user"
    }
}