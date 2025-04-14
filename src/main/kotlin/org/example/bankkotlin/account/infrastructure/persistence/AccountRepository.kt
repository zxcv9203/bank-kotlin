package org.example.bankkotlin.account.infrastructure.persistence

import org.example.bankkotlin.account.domain.Account
import org.example.bankkotlin.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, String> {
    fun findByUlid(ulid: String): Account?
    fun findByUlidAndUser(ulid: String, user: User): Account?
}