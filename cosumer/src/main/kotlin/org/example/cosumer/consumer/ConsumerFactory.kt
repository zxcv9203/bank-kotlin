package org.example.cosumer.consumer

import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.stereotype.Component

@Component
class ConsumerFactory(
    @Qualifier("factoryHandlerMapper")
    private val factoryHandlerMapper: Map<String, ConcurrentMessageListenerContainer<String, Any>>
) {
    private val logger: Logger = org.slf4j.LoggerFactory.getLogger(ConsumerFactory::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun startConsumers() {
        factoryHandlerMapper.forEach { (topicName, container) ->
            try {
                container.start()
                logger.info("Started consumer $topicName")
            } catch (e: Exception) {
                logger.error("Failed to start consumer for topic $topicName", e)
            }
        }
    }
}