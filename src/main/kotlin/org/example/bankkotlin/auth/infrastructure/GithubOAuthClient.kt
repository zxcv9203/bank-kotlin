package org.example.bankkotlin.auth.infrastructure

import org.example.bankkotlin.auth.domain.OAuthClient
import org.example.bankkotlin.auth.infrastructure.GoogleOAuthClient.Companion
import org.example.bankkotlin.auth.infrastructure.config.OAuth2Config
import org.example.bankkotlin.auth.infrastructure.response.OAuth2TokenResponse
import org.example.bankkotlin.auth.infrastructure.response.OAuth2UserResponse
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.springframework.stereotype.Component

@Component(GithubOAuthClient.CLIENT_ID)
class GithubOAuthClient(
    private val config: OAuth2Config,
) : OAuthClient {

    private val oAuthInfo = config.providers[CLIENT_ID]
        ?: throw CustomException(ErrorCode.AUTH_CONFIG_NOT_FOUND, GoogleOAuthClient.CLIENT_ID)

    override val providerName: String
        get() = CLIENT_ID

    override fun getToken(code: String): OAuth2TokenResponse {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(accessToken: String): OAuth2UserResponse {
        TODO("Not yet implemented")
    }

    companion object {
        const val CLIENT_ID = "github"
    }
}