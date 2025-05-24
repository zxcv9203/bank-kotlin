package org.example.cosumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CosumerApplication

fun main(args: Array<String>) {
    runApplication<CosumerApplication>(*args)
}
