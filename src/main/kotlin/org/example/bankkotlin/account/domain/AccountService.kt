package org.example.bankkotlin.account.domain

import org.example.bankkotlin.account.infrastructure.persistence.AccountRepository
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
) {

    fun save(account: Account): Account {
        return accountRepository.save(account)
    }

    fun getByUlid(ulid: String): Account {
        return accountRepository.findByUlid(ulid)
            ?: throw CustomException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found with ulid: $ulid")
    }
}