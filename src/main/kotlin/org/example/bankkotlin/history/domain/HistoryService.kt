package org.example.bankkotlin.history.domain

import kotlinx.serialization.builtins.ListSerializer
import org.example.bankkotlin.common.cache.RedisClient
import org.example.bankkotlin.common.cache.RedisKeyProvider
import org.example.bankkotlin.common.util.JsonUtils
import org.example.bankkotlin.common.util.Logging
import org.example.bankkotlin.history.repository.HistoryMongoRepository
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class HistoryService(
    private val historyMongoRepository: HistoryMongoRepository,
    private val redisClient: RedisClient,
) {
    private val logger: Logger = Logging.getLogger(HistoryService::class.java)

    fun history(ulid: String): List<History> = Logging.logFor(logger) { it ->
        it["ulid"] = ulid

        val key = RedisKeyProvider.historyCacheKey(ulid)
        val cacheValue = redisClient.get(key)
        return@logFor when {
            cacheValue == null -> {
                val result = historyMongoRepository.findLatestTransactionHistory(ulid)
                redisClient.setIfNotExists(key, JsonUtils.encodeToJson(result, ListSerializer(History.serializer())))
                result
            }

            else -> {
                JsonUtils.decodeFromJson(cacheValue, ListSerializer(History.serializer()))
            }
        }
    }
}