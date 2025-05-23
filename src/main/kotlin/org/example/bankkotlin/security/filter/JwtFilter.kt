package org.example.bankkotlin.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.bankkotlin.auth.domain.JwtProvider
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.example.bankkotlin.common.model.Response
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.PathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtProvider: JwtProvider,
    private val pathMatcher: PathMatcher
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestURI = request.requestURI

        if (shouldPerformAuthentication(requestURI)) {
            val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                val token = authHeader.substring(7)
                try {
                    jwtProvider.verifyToken(token)
                } catch (e: CustomException) {
                    response.status = HttpStatus.UNAUTHORIZED.value()
                    response.contentType = MediaType.APPLICATION_JSON_VALUE

                    val message = e.message
                    val errorResponse = Response.failed(
                        code = HttpStatus.UNAUTHORIZED,
                        message = message ?: ErrorCode.ACCESS_TOKEN_NEED.message,
                        data = Unit
                    )
                    response.writer.write(ObjectMapper().writeValueAsString(errorResponse))
                    response.writer.flush()
                }
            } else {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = MediaType.APPLICATION_JSON_VALUE

                val errorResponse = Response.failed(
                    code = HttpStatus.UNAUTHORIZED,
                    message = ErrorCode.ACCESS_TOKEN_NEED.message,
                    data = Unit
                )
                response.writer.write(ObjectMapper().writeValueAsString(errorResponse))
                response.writer.flush()
            }
        }
        filterChain.doFilter(request, response)
    }


    private fun shouldPerformAuthentication(uri: String): Boolean {
        for (endPoint in JWT_AUTH_ENDPOINT) {
            if (pathMatcher.match(endPoint, uri)) {
                return true
            }
        }
        return false
    }

    companion object {
        private val JWT_AUTH_ENDPOINT = arrayOf(
            "/api/v1/users/**",
            "/api/v1/transactions/**",
            "/api/v1/histories/**",
        )
    }
}