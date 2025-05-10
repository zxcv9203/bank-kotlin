package org.example.bankkotlin.history.repository

import org.example.bankkotlin.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface HistoryUserRepository : JpaRepository<User, String> {
    fun findByUlid(ulid: String): User?
}