package org.example.bankkotlin.security.domain

import org.example.bankkotlin.security.infrastructure.response.OAuth2TokenResponse
import org.example.bankkotlin.security.infrastructure.response.OAuth2UserResponse

interface OAuthClient {
    val providerName: String
    fun getToken(code: String): OAuth2TokenResponse
    fun getUserInfo(accessToken: String): OAuth2UserResponse
}
