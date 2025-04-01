package org.example.bankkotlin.security.infrastructure

import org.example.bankkotlin.security.domain.OAuthClient
import org.example.bankkotlin.security.infrastructure.config.OAuth2Config
import org.example.bankkotlin.security.infrastructure.response.OAuth2TokenResponse
import org.example.bankkotlin.security.infrastructure.response.OAuth2UserResponse
import org.springframework.stereotype.Component

@Component(GithubOAuthClient.CLIENT_ID)
class GithubOAuthClient(
    private val config: OAuth2Config,
) : OAuthClient {

    private val oAuthInfo = config.providers[CLIENT_ID]
        ?: throw IllegalArgumentException("OAuth2 provider not found: $CLIENT_ID")

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