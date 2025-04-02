package org.example.bankkotlin.auth.infrastructure.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GithubOAuth2UserClientResponse(
    private val id: Int,
    @SerialName("repos_url")
    private val reposUrl: String,
    private val name: String,
) {
    fun toOAuth2UserResponse(): GithubOAuth2UserResponse {
        return GithubOAuth2UserResponse(
            id = id.toString(),
            email = reposUrl,
            name = name,
        )
    }
}

@Serializable
data class GithubOAuth2UserResponse(
    override val id: String,
    override val email: String?,
    override val name: String?,
) : OAuth2UserResponse {
}

