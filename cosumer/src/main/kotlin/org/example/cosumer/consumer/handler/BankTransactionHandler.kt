package org.example.cosumer.consumer.handler

import kotlinx.serialization.builtins.ListSerializer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.example.cosumer.common.cache.RedisClient
import org.example.cosumer.common.cache.RedisKeyProvider
import org.example.cosumer.common.json.JsonUtils
import org.example.cosumer.consumer.repository.HistoryMongoRepository
import org.example.cosumer.entrypoint.Handler
import org.example.cosumer.types.dto.TransactionMessage
import org.example.cosumer.types.entity.History
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class BankTransactionHandler(
    private val historyMongoRepository: HistoryMongoRepository,
    private val redisClient: RedisClient,
) : Handler {
    private val logger: Logger = LoggerFactory.getLogger(BankTransactionHandler::class.java)
    override fun handle(
        record: ConsumerRecord<String, Any>,
        acknowledged: Acknowledgment?
    ) {
        acknowledged?.let { it ->
            val rawMessage = record.value()
            logger.info("Received message from ${record.topic()}: $rawMessage")
            try {
                val messageString = when (rawMessage) {
                    is String -> rawMessage
                    else -> rawMessage.toString()
                }
                val data = JsonUtils.decodeFromJson(messageString, TransactionMessage.serializer())
                val entity = data.toEntity()
                historyMongoRepository.saveTransactionHistory(entity)

                handlerRedisData(data.fromUlid, data.toDto())
                handlerRedisData(data.toUlid, data.toDto())

                it.acknowledge()

                logger.info("Successfully saved $rawMessage")
            } catch (e: Exception) {
                logger.error("Failed to process message: $rawMessage", e)
            }
        }
    }

    fun handlerRedisData(ulid: String, dto: History) {
        val cacheKey = RedisKeyProvider.historyCacheKey(ulid)
        val cachedValue = redisClient.get(cacheKey)

        if (cachedValue != null) {
            val cachedData: List<History> = JsonUtils.decodeFromJson(cachedValue, ListSerializer(History.serializer()))

            val updatedHistoryList = if (cachedData.size == 30) {
                val mutableList = cachedData.toMutableList()
                mutableList.removeAt(mutableList.size - 1)
                mutableList.add(0, dto)
                mutableList
            } else {
                val mutableList = cachedData.toMutableList()
                mutableList.add(0, dto)
                mutableList
            }
            val updatedData = JsonUtils.encodeToJson(updatedHistoryList, ListSerializer(History.serializer()))
            redisClient.set(cacheKey, updatedData)
        }
    }

    override fun handleDLQ(
        record: ConsumerRecord<String, Any>,
        acknowledged: Acknowledgment?
    ) {
        TODO("Not yet implemented")
    }

}