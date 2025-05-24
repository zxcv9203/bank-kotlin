package org.example.cosumer.entrypoint

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.support.Acknowledgment

interface Handler {
    fun handle(record: ConsumerRecord<String, Any>, acknowledged: Acknowledgment)
}