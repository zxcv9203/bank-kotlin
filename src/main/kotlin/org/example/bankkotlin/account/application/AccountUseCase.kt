package org.example.bankkotlin.account.application

import org.example.bankkotlin.account.domain.AccountService
import org.example.bankkotlin.common.util.Logging
import org.example.bankkotlin.common.util.Transactional
import org.example.bankkotlin.user.domain.UserService
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AccountUseCase(
    private val transactional: Transactional,
    private val userService: UserService,
    private val accountService: AccountService,
) {
    private val log: Logger = Logging.getLogger(AccountUseCase::class.java)

    fun create(ulid: String) = Logging.logFor(log) { log ->
        log["userUlid"] = ulid
        transactional.run {
        }
        return@logFor ""
    }

    fun balance(userUlid: String, accountUlid: String) = Logging.logFor(log) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        return@logFor BigDecimal.ZERO
    }

    fun remove(userUlid: String, accountUlid: String) = Logging.logFor(log) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        return@logFor ""
    }
}