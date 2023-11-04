package com.mospolytech.domain.base.utils.generators

import kotlin.random.Random

object Generator {
    private val charPoolName: List<Char> = ('а'..'я') + ('А'..'я') + listOf(' ')
    private val charPoolId: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun randomString(length: Int = 20): String =
        (1..length)
            .map { _ -> Random.nextInt(0, charPoolName.size) }
            .map(charPoolName::get)
            .joinToString("")

    fun generateStringId(length: Int = 10): String =
        (1..length)
            .map { _ -> Random.nextInt(0, charPoolId.size) }
            .map(charPoolId::get)
            .joinToString("")

    fun generateId(length: Int = 4): Int {
        return Random.nextInt(1, (length + 1) * 10 - 1)
    }
}
