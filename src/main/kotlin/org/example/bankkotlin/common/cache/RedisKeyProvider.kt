package org.example.bankkotlin.common.cache

object RedisKeyProvider {
    private const val BANK_MUTEX_KEY = "bankMutex"
    private const val HISOTRY_CACHE_KEY = "history"

    fun bankMetuxKey(ulid: String, accountUlid: String): String {
        return "$BANK_MUTEX_KEY:$ulid:$accountUlid"
    }

    fun historyCacheKey(ulid: String, accountUlid: String): String {
        return "$HISOTRY_CACHE_KEY:$ulid:$accountUlid"
    }
}