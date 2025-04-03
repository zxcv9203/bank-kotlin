package org.example.bankkotlin.auth.infrastructure.web.response

interface OAuth2UserResponse {
    val id: String
    val email: String?
    val name: String?
}