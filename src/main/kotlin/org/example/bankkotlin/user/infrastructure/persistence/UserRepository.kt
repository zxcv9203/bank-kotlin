package org.example.bankkotlin.user.infrastructure.persistence

import org.example.bankkotlin.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, String> {
    fun existsByUsername(username: String): Boolean

    @Modifying
    @Query("UPDATE User u SET u.accessToken = :accessToken WHERE u.username = :username")
    fun updateAccessTokenByUsername(username: String, accessToken: String)
}