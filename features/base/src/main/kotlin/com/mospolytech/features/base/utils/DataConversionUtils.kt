package com.mospolytech.features.base.utils

import kotlin.reflect.full.createType

inline fun <reified T : Enum<T>>
        io.ktor.util.converters.DataConversion.Configuration.setEnumConverter(
    crossinline values1: () -> Array<T>
) {
    convert<T>(T::class.createType()) {
        encode {
            listOf(
                it.name.flatMapIndexed { index, c ->
                    if (c.isUpperCase()) {
                        if (index == 0) {
                            listOf(c.lowercaseChar())
                        } else {
                            listOf('-', c.lowercaseChar())
                        }
                    } else {
                        listOf(it)
                    }

                }.toString()
            )
        }
        decode { values ->
            values1().first { enum ->
                values.any { it.replace("-", "").equals(enum.name, ignoreCase = true) }
            }
        }
    }
}