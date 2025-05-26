package org.example.cosumer.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka

@Configuration
@EnableKafka
class KafkaConsumerConfig(
    private val topicConfig: TopicConfig,
) {
    private val logger: Logger = LoggerFactory.getLogger(KafkaConsumerConfig::class.java)

    @Bean
    fun consumerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()

        // 고정 값
        props[ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG] = "500" // 연결이 실패하는 경우에 대해서 재연결 시도 간격
        props[ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG] = "5000" // 최대 재연결 간격
        props[ConsumerConfig.RETRY_BACKOFF_MS_CONFIG] = "500"  // 실패한 요청에 대해서 재시도 간격
        props[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = "30000" // 컨슈머와 브로커와의 세션을 유지하는 최대 시간
        props[ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG] = "3000" // 헬스체크 전송 간격

        // config value
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = topicConfig.info.bootstrapServers // 연결하고자 하는 서버 url
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] =
            topicConfig.info.consumer.autoOffsetReset // 초기에 오프셋 위치를 설정하는 earliest, latest
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = topicConfig.info.consumer.autoCommit // auto commit

        props[ConsumerConfig.GROUP_ID_CONFIG] = topicConfig.info.consumer.groupId

        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

        return props
    }
}