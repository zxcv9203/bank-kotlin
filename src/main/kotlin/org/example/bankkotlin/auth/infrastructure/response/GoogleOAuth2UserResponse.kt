package org.example.bankkotlin.auth.infrastructure.response

import kotlinx.serialization.Serializable

@Serializable
data class GoogleOAuth2UserResponse(
    override val id: String,
    override val email: String,
    override val name: String,
) : OAuth2UserResponse