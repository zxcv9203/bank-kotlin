package org.example.bankkotlin.security.infrastructure.response

interface OAuth2UserResponse {
    val id: String
    val email: String?
    val name: String?
}