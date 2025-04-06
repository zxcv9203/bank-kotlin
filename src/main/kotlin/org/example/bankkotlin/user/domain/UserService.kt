package org.example.bankkotlin.user.domain

import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
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

    fun getByUlid(ulid: String): User {
        return userRepository.findByUlid(ulid)
            ?: throw CustomException(ErrorCode.USER_NOT_FOUND, "User not found with ulid: $ulid")
    }
}