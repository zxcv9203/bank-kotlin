package org.example.bankkotlin.security.infrastructure.model

data class OAuthProvider(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
)