package org.example.bankkotlin.account.application

import com.github.f4b6a3.ulid.UlidCreator
import org.example.bankkotlin.account.domain.Account
import org.example.bankkotlin.account.domain.AccountService
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.example.bankkotlin.common.util.Logging
import org.example.bankkotlin.common.util.Transactional
import org.example.bankkotlin.user.domain.UserService
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.lang.Math.random
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
            val user = userService.getByUlid(ulid)

            val accountUlid = UlidCreator.getUlid().toString()
            val accountNumber = generateRandomAccountNumber()

            val account = Account(
                ulid = accountUlid,
                accountNumber = accountNumber,
                user = user,
            )

            try {
                accountService.save(account)
            } catch (e: Exception) {
                throw CustomException(ErrorCode.FAILED_TO_SAVE_DATA, e.message)
            }
        }
        return@logFor "SUCCESS"
    }

    fun balance(userUlid: String, accountUlid: String) = Logging.logFor(log) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        return@logFor transactional.run {
            val account = accountService.getByUlid(accountUlid)
            if (account.user.ulid != userUlid) {
                throw CustomException(
                    ErrorCode.MISS_MATCH_ACCOUNT_ULID_AND_USER_ULID,
                    "input : $userUlid, db : ${account.user.ulid}"
                )
            }
            return@run account.balance
        }
    }

    fun remove(userUlid: String, accountUlid: String) = Logging.logFor(log) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        return@logFor transactional.run {
            val account = accountService.getByUlid(accountUlid)
            if (account.user.ulid != userUlid) {
                throw CustomException(
                    ErrorCode.MISS_MATCH_ACCOUNT_ULID_AND_USER_ULID,
                    "input : $userUlid, db : ${account.user.ulid}"
                )
            }
            if (BigDecimal.ZERO.compareTo(account.balance) != 0) {
                throw CustomException(
                    ErrorCode.ACCOUNT_BALANCE_IS_NOT_ZERO,
                    "accountUlid: $accountUlid"
                )
            }
            account.delete()
            try {
                accountService.save(account)
            } catch (e: Exception) {
                throw CustomException(ErrorCode.FAILED_TO_SAVE_DATA, e.message)
            }
            return@run "SUCCESS"
        }
    }

    private fun generateRandomAccountNumber(): String {
        val bankCode = "003"
        val section = "12"
        val number = random().toString()
        return "$bankCode-$section-$number"
    }
}