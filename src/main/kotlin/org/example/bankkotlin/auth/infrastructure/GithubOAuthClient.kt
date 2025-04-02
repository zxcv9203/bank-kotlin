package org.example.bankkotlin.auth.infrastructure

import okhttp3.FormBody
import org.example.bankkotlin.auth.domain.OAuthClient
import org.example.bankkotlin.auth.infrastructure.config.OAuth2Config
import org.example.bankkotlin.auth.infrastructure.response.OAuth2TokenResponse
import org.example.bankkotlin.auth.infrastructure.response.OAuth2UserResponse
import org.example.bankkotlin.common.client.HttpClient
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
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

        // json 처리
    }

    override fun getUserInfo(accessToken: String): OAuth2UserResponse {
        TODO("Not yet implemented")
    }

    companion object {
        const val CLIENT_ID = "github"
        const val TOKEN_URL = "https://github.com/login/oauth/access_token"
        const val USER_INFO_URL = "https://api.github.com/user"
    }
}