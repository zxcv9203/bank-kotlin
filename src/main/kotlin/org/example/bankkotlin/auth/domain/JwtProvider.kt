package org.example.bankkotlin.auth.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.InvalidClassException
import java.security.SignatureException
import java.util.*

@Component
class JwtProvider(
    @Value("\${jwt.secret-key}")
    private val secretKey: String,
    @Value("\${jwt.time}")
    private val time: Long,
) {

    fun createToken(platform: String, email: String?, name: String?, id: String): String {
        return JWT.create()
            .withSubject("$platform - $email - $name - $id")
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + time * ONE_MINUTE_TO_MILLIS))
            .sign(Algorithm.HMAC256(secretKey))
    }

    fun verifyToken(token: String): DecodedJWT {
        try {
            return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
        } catch (e: AlgorithmMismatchException) {
            throw CustomException(ErrorCode.TOKEN_IS_INVALID, e.message)
        } catch (e: SignatureException) {
            throw CustomException(ErrorCode.TOKEN_IS_INVALID, e.message)
        } catch (e: InvalidClassException) {
            throw CustomException(ErrorCode.TOKEN_IS_INVALID, e.message)
        } catch (e: TokenExpiredException) {
            throw CustomException(ErrorCode.TOKEN_IS_EXPIRED, e.message)
        }
    }

    companion object {
        const val ONE_MINUTE_TO_MILLIS: Long = 60 * 1000
    }
}