package org.example.bankkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BankKotlinApplication

fun main(args: Array<String>) {
    runApplication<BankKotlinApplication>(*args)
}
