package org.example.bankkotlin.account.application

import org.example.bankkotlin.common.util.Logging
import org.example.bankkotlin.common.util.Transactional
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class AccountApplicationService(
    private val transactional: Transactional,
) {
    private val log: Logger = Logging.getLogger(AccountApplicationService::class.java)

    fun create(ulid: String) = Logging.logFor(log) { log ->
        log["userUlid"] = ulid
        transactional.run {

        }
    }

    fun balance(userUlid: String, accountUlid: String) = Logging.logFor(log) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
    }

    fun remove(userUlid: String, accountUlid: String) = Logging.logFor(log) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
    }
}