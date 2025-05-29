package org.example.cosumer.consumer.repository

import org.example.cosumer.config.MongoTableCollector
import org.example.cosumer.exception.CustomException
import org.example.cosumer.exception.ErrorCode
import org.example.cosumer.types.message.TransactionHistoryDocument
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class HistoryMongoRepository(
    private val template: HashMap<String, MongoTemplate>
) {

    fun saveTransactionHistory(
        event: TransactionHistoryDocument
    ) {

        val template = template(MongoTableCollector.Bank)
        template.save(event)
    }

    private fun template(c: MongoTableCollector): MongoTemplate {
        val table = template[c.tableName]

        table?.let { return it }

        throw CustomException(ErrorCode.MONGO_TEMPLATE_NOT_FOUND)
    }
}