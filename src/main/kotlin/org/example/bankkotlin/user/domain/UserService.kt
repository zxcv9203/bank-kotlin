package org.example.bankkotlin.user.domain

import org.example.bankkotlin.user.infrastructure.persistence.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    fun updateAccessTokenByUsername(username: String, accessToken: String) {
        userRepository.updateAccessTokenByUsername(username, accessToken)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }
}