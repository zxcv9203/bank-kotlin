package org.example.bankkotlin.auth.application

import com.github.f4b6a3.ulid.UlidCreator
import org.example.bankkotlin.auth.domain.JwtProvider
import org.example.bankkotlin.auth.domain.OAuthClient
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.example.bankkotlin.common.util.Logging
import org.example.bankkotlin.common.util.Transactional
import org.example.bankkotlin.user.domain.User
import org.example.bankkotlin.user.domain.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val oAuth2Clients: Map<String, OAuthClient>,
    private val jwtProvider: JwtProvider,
    private val transactional: Transactional,
    private val userService: UserService
) {
    private val logger: Logger = LoggerFactory.getLogger(AuthService::class.java)

    fun handle(state: String, code: String): String = Logging.logFor(logger) { log ->
        val provider = state.lowercase()
        log["provider"] = provider
        val callService = oAuth2Clients[provider] ?: throw CustomException(ErrorCode.PROVIDER_NOT_FOUND, provider)
        val tokenResponse = callService.getToken(code)
        val userInfo = callService.getUserInfo(tokenResponse.accessToken)
        val token = jwtProvider.createToken(provider, userInfo.email, userInfo.name, userInfo.id)

        val username = (userInfo.name ?: userInfo.email).toString()
        transactional.run {
            val exist = userService.existsByUsername(username)
            if (exist) {
                userService.updateAccessTokenByUsername(username, token)
            } else {
                val ulid = UlidCreator.getUlid().toString()

                val user = User(
                    ulid = ulid,
                    username = username,
                    accessToken = token
                )
                userService.save(user)
            }
        }

        return@logFor token
    }
}