package org.example.bankkotlin.account.domain

import org.example.bankkotlin.account.infrastructure.persistence.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
) {
}