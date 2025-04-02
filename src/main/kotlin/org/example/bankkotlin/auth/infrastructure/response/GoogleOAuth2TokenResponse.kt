package org.example.bankkotlin.auth.infrastructure.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleOAuth2TokenResponse(
    @SerialName("access_token")
    override val accessToken: String,
) : OAuth2TokenResponse
