package org.example.bankkotlin.auth.api

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.example.bankkotlin.auth.application.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {

    @GetMapping("/callback")
    fun callback(
        @RequestParam("code", required = true) code: String,
        @RequestParam("state", required = true) state: String,
        response: HttpServletResponse
    ): ResponseEntity<Map<String, String>> {
        val token: String = authService.handle(code = code, state = state)

        response.addCookie(
            Cookie("authToken", token).apply {
                isHttpOnly = true
                path = "/"
                maxAge = 60 * 60 * 24
            }
        )

        // TODO -> create url을 config로 관리
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("http://localhost:3000"))
            .build()
    }

    @GetMapping("/verifiy-token")
    fun verifyToken(
        @RequestHeader("Authorization") authHeader: String,
    ) {
        authService.verifyToken(authHeader)
    }
}