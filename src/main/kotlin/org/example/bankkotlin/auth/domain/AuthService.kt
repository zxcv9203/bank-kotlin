package org.example.bankkotlin.auth.domain

import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val oAuth2Clients: Map<String, OAuthClient>,
    private val jwtProvider: JwtProvider,
) {

    fun handle(state: String, code: String): String {
        val provider = state.lowercase()

        val callService = oAuth2Clients[provider] ?: throw CustomException(ErrorCode.PROVIDER_NOT_FOUND, provider)

        val tokenResponse = callService.getToken(code)
        val userInfo = callService.getUserInfo(tokenResponse.accessToken)
        val token = jwtProvider.createToken(provider, userInfo.email, userInfo.name, userInfo.id)

        return token
    }
}