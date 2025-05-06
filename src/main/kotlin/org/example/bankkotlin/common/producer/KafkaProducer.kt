package org.example.bankkotlin.common.producer

import org.example.bankkotlin.common.exception.CustomException
import org.example.bankkotlin.common.exception.ErrorCode
import org.example.bankkotlin.common.util.Logging
import org.slf4j.Logger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class KafkaProducer(
    private val template: KafkaTemplate<String, Any>,
) {
    private val log: Logger = Logging.getLogger(KafkaProducer::class.java)

    fun sendMessage(topic: String, message: Any) {
        val future = template.send(topic, message)

        future.whenComplete { result, ex ->
            if (ex == null) {
                log.info("메시지 발행 성공 : $topic, $message, timestamp : ${LocalDateTime.now()}")
            } else {
                throw CustomException(ErrorCode.FAILED_TO_SEND_MESSAGE, topic)
            }
        }
    }
}