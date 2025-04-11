package org.example.bankkotlin.common.cache

import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.redisson.api.RedissonClient
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

class RedisClient(
    private val template: RedisTemplate<String, String>,
    private val redissonClient: RedissonClient
) {

    fun get(key: String): String? {
        return template.opsForValue()[key]
    }

    fun <T> get(key: String, kSerializer: (Any) -> T?): T? {
        val value = template.opsForValue()[key]

        return value?.let { kSerializer(it) }
    }

    fun setIfNotExists(key: String, value: String): Boolean {
        return template.opsForValue().setIfAbsent(key, value) ?: false
    }

    fun <T> invokeWithMutex(key: String, function: () -> T?) {
        val lock = redissonClient.getLock(key)

        try {
            lock.lock(15, TimeUnit.SECONDS)
            function.invoke()
        } catch (e: Exception) {
            throw CustomException(ErrorCode.FAILED_TO_MUTEX_INVOKE, key)
        } finally {
            lock.unlock()
        }
    }
}