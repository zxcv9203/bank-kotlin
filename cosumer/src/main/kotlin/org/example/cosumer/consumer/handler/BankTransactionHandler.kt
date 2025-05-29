package org.example.cosumer.consumer.handler

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.example.cosumer.common.cache.RedisClient
import org.example.cosumer.consumer.repository.HistoryMongoRepository
import org.example.cosumer.entrypoint.Handler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class BankTransactionHandler(
    private val historyMongoRepository: HistoryMongoRepository,
    private val redisClient: RedisClient,
): Handler {
    private val logger: Logger = LoggerFactory.getLogger(BankTransactionHandler::class.java)
    override fun handle(
        record: ConsumerRecord<String, Any>,
        acknowledged: Acknowledgment?
    ) {
        TODO("Not yet implemented")
    }

    override fun handleDLQ(
        record: ConsumerRecord<String, Any>,
        acknowledged: Acknowledgment?
    ) {
        TODO("Not yet implemented")
    }

}