package org.example.bankkotlin.common.cache

import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.redisson.api.RedissonClient
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
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

    fun <T> invokeWithMutex(key: String, function: () -> T?): T? {
        val lock = redissonClient.getLock(key)
        var lockAcquired = false

        try {
            lockAcquired = lock.tryLock(10, 15, TimeUnit.SECONDS)
            if (!lockAcquired) {
                throw CustomException(ErrorCode.FAILED_TO_GET_LOCK, key)
            }
            return function.invoke()
        } catch (e: Exception) {
            throw CustomException(ErrorCode.FAILED_TO_MUTEX_INVOKE, e.message)
        } finally {
            if (lockAcquired && lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }
}