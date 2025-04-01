package org.example.bankkotlin.auth.infrastructure.model

data class OAuthProvider(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
)