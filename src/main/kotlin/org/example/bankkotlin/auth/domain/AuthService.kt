package org.example.bankkotlin.auth.domain

import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.example.bankkotlin.common.util.Logging
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val oAuth2Clients: Map<String, OAuthClient>,
    private val jwtProvider: JwtProvider,
) {
    private val logger: Logger = LoggerFactory.getLogger(AuthService::class.java)

    fun handle(state: String, code: String): String = Logging.logFor(logger) { log ->
        val provider = state.lowercase()
        log["provider"] = provider
        val callService = oAuth2Clients[provider] ?: throw CustomException(ErrorCode.PROVIDER_NOT_FOUND, provider)
        val tokenResponse = callService.getToken(code)
        val userInfo = callService.getUserInfo(tokenResponse.accessToken)
        val token = jwtProvider.createToken(provider, userInfo.email, userInfo.name, userInfo.id)

        token
    }
}