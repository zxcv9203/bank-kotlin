package org.example.bankkotlin.account.application

import org.example.bankkotlin.account.application.model.TransactionMessage
import org.example.bankkotlin.account.application.model.TransferResult
import org.example.bankkotlin.account.domain.AccountService
import org.example.bankkotlin.common.cache.RedisClient
import org.example.bankkotlin.common.cache.RedisKeyProvider
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.example.bankkotlin.common.producer.KafkaProducer
import org.example.bankkotlin.common.util.JsonUtils
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
    private val kafkaProducer: KafkaProducer,
) {
    private val log = Logging.getLogger(AccountTransactionUseCase::class.java)

    fun deposit(userUlid: String, accountId: String, value: BigDecimal) = Logging.logFor(log) {
        it["userUlid"] = userUlid
        it["accountId"] = accountId
        it["value"] = value

        val key = RedisKeyProvider.bankMetuxKey(userUlid, accountId)

        return@logFor redisClient.invokeWithMutex(key) {
            transactional.run {
                val user = userService.getByUlid(userUlid)
                val account = accountService.getByUlidAndUser(accountId, user)
                account.balance = account.balance.add(value)
                val updatedAccount = accountService.save(account)

                val message = JsonUtils.encodeToJson(
                    TransactionMessage(
                        fromUlid = "0x0",
                        fromName = "0x0",
                        fromAccountId = "0x0",
                        toUlid = user.ulid,
                        toName = user.username,
                        toAccountId = accountId,
                        amount = value
                    ), TransactionMessage.serializer()
                )
                kafkaProducer.sendMessage("", message)
                return@run updatedAccount
            }
        }
    }

    fun transfer(fromUlid: String, fromAccountId: String, toAccountId: String, value: BigDecimal): TransferResult =
        Logging.logFor(log) {
            it["fromUlid"] = fromUlid
            it["fromAccountId"] = fromAccountId
            it["toAccountId"] = toAccountId
            it["value"] = value

            val key = RedisKeyProvider.bankMetuxKey(fromUlid, fromAccountId)

            return@logFor redisClient.invokeWithMutex(key) {
                return@invokeWithMutex transactional.run {
                    val fromAccount = accountService.getByUlid(fromAccountId)

                    if (fromAccount.user.ulid != fromUlid) {
                        throw CustomException(
                            ErrorCode.MISS_MATCH_ACCOUNT_ULID_AND_USER_ULID,
                            "input : $fromUlid, db : ${fromAccount.user.ulid}"
                        )
                    } else if (fromAccount.balance < value) {
                        throw CustomException(
                            ErrorCode.ENOUGH_BALANCE,
                            "input : $value, db : ${fromAccount.balance}"
                        )
                    } else if (value <= BigDecimal.ZERO) {
                        throw CustomException(
                            ErrorCode.VALUE_MUST_BE_POSITIVE,
                            "input : $value, db : ${fromAccount.balance}"
                        )
                    }
                    val toAccount = accountService.getByUlid(toAccountId)

                    fromAccount.balance = fromAccount.balance.subtract(value)
                    toAccount.balance = toAccount.balance.add(value)

                    accountService.save(toAccount)
                    accountService.save(fromAccount)

                    return@run TransferResult(
                        toAccountBalance = toAccount.balance,
                        fromAccountBalance = fromAccount.balance
                    )
                }
            }
        }
}