package org.example.bankkotlin.account.infrastructure.persistence

import org.example.bankkotlin.account.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, String> {
    fun findByUlid(ulid: String): Account?
}