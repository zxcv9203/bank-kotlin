package org.example.bankkotlin.common.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ReadPreference
import com.mongodb.client.MongoClients
import org.bson.UuidRepresentation
import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Configuration
@EnableMongoAuditing
class MongoConfig(
    @Value("\${database.mongo.uri}")
    val url: String
) {
    @Bean
    fun template(): HashMap<String, MongoTemplate> {
        val mapper = HashMap<String, MongoTemplate>(MongoTableCollector.entries.size)

        for (table in MongoTableCollector.entries) {
            val settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD) // BSON 표준
                .applyConnectionString(ConnectionString(url))
                .readPreference(ReadPreference.primary()) // 모든 읽기 작업이 primary 노드에서 수행
                .build()

            try {
                val client = MongoClients.create(settings)
                mapper[table.tableName] = MongoTemplate(SimpleMongoClientDatabaseFactory(client, table.tableName))
            } catch (e: Exception) {
                throw CustomException(ErrorCode.FAILED_TO_CONNECT_MONGODB, e.message)
            }
        }
        return mapper
    }
}