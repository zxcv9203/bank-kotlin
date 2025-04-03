package org.example.bankkotlin.common.util

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

interface Runner {
    fun <T> run(function: () -> T?): T?
    fun <T> readOnly(function: () -> T?): T?
}

@Component
class Transactional(
    private val advice: Runner = Advice(),
) {

    @Transactional
    fun <T> run(function: () -> T?): T? = advice.run(function)

    @Transactional(readOnly = true)
    fun <T> readOnly(function: () -> T?): T? = advice.readOnly(function)

    @Component
    private class Advice : Runner {
        @Transactional
        override fun <T> run(function: () -> T?): T? = function()

        @Transactional(readOnly = true)
        override fun <T> readOnly(function: () -> T?): T? = function()

    }
}