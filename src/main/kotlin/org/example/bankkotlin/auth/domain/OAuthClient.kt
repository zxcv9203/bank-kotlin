package org.example.bankkotlin.auth.domain

import org.example.bankkotlin.auth.infrastructure.response.OAuth2TokenResponse
import org.example.bankkotlin.auth.infrastructure.response.OAuth2UserResponse

interface OAuthClient {
    val providerName: String
    fun getToken(code: String): OAuth2TokenResponse
    fun getUserInfo(accessToken: String): OAuth2UserResponse
}
