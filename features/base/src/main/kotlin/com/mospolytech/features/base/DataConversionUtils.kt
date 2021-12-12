package com.mospolytech.features.base

import kotlin.reflect.full.createType

inline fun <reified T : Enum<T>>
        io.ktor.util.converters.DataConversion.Configuration.setEnumConverter(
    crossinline values1: () -> Array<T>
) {
    convert<T>(T::class.createType()) {
        encode { listOf(it.name.lowercase()) }
        decode { values -> values1().first { enum -> values.any { it.equals(enum.name, ignoreCase = true) } } }
    }
}