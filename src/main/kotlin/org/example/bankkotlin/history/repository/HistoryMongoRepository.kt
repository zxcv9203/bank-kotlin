package org.example.bankkotlin.history.repository

import org.example.bankkotlin.common.config.MongoTableCollector
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.example.bankkotlin.history.domain.History
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class HistoryMongoRepository(
    private val template: HashMap<String, MongoTemplate>,
    private val historyUserRepository: HistoryUserRepository,
) {
    private val userNameMapper: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    fun findLatestTransactionHistory(ulid: String): List<History> {
        val criteria = Criteria().orOperator(
            Criteria.where("fromUlid").`is`(ulid),
            Criteria.where("toUlid").`is`(ulid)
        )

        val query = Query(criteria)
            .with(Sort.by(Sort.Direction.DESC, "createdAt"))
            .limit(30)

        query.fields().exclude("_id")
        val result: List<TransactionHistoryDocument> = getTemplate(MongoTableCollector.Bank)
            .find(query, TransactionHistoryDocument::class.java)

        return result.map { doc ->
            val fromUser = getUserName(doc.fromUlid)
            val toUser = getUserName(doc.toUlid)
            doc.toResponse(fromUser, toUser)
        }
    }

    private fun getUserName(ulid: String): String {
        val value = userNameMapper[ulid] ?: ""
        if (value.isEmpty()) {
            val user = historyUserRepository.findByUlid(ulid)
                ?: throw CustomException(
                    ErrorCode.USER_NOT_FOUND,
                    "User not found with ulid: $ulid"
                )
            userNameMapper[ulid] = user.username
            return user.username
        } else {
            return value
        }
    }

    private fun getTemplate(c: MongoTableCollector): MongoTemplate {
        return template[c.tableName]
            ?: throw CustomException(
                ErrorCode.MONGO_TEMPLATE_NOT_FOUND,
                "MongoTemplate not found for table: ${c.tableName}"
            )
    }
}