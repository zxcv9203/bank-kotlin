package org.example.bankkotlin.account.application

import org.example.bankkotlin.account.domain.AccountService
import org.example.bankkotlin.common.cache.RedisClient
import org.example.bankkotlin.common.cache.RedisKeyProvider
import org.example.bankkotlin.common.util.Logging
import org.example.bankkotlin.common.util.Transactional
import org.example.bankkotlin.user.domain.UserService
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class AccountTransactionUseCase(
    private val userService: UserService,
    private val accountService: AccountService,
    private val redisClient: RedisClient,
    private val transactional: Transactional,
) {
    private val log = Logging.getLogger(AccountTransactionUseCase::class.java)

    fun deposit(userUlid: String, accountId: String, value: BigDecimal) = Logging.logFor(log) {
        it["userUlid"] = userUlid
        it["accountId"] = accountId
        it["value"] = value

        val key = RedisKeyProvider.bankMetuxKey(userUlid, accountId)

        redisClient.invokeWithMutex(key) {
            return@invokeWithMutex transactional.run {
                val user = userService.getByUlid(userUlid)
                val account = accountService.getByUlidAndUser(accountId, user)
                account.balance = account.balance.add(value)
                accountService.save(account)
            }
        }
    }
}