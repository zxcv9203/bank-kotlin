package org.example.bankkotlin.auth.infrastructure.web.config

import org.example.bankkotlin.auth.infrastructure.web.model.OAuthProvider
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "oauth2")
class OAuth2Config {
    val providers: MutableMap<String, OAuthProvider> = mutableMapOf()
}

