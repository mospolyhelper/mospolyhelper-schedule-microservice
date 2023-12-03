package com.mospolytech.domain.base.utils

import java.util.*

/**
 * Replacement for Kotlin's deprecated `capitalize()` function.
 */
fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(Locale.getDefault())
        } else {
            it.toString()
        }
    }
}

/**
 * Replacement for Kotlin's deprecated `capitalize()` function.
 */
fun String.decapitalized(): String {
    return this.replaceFirstChar {
        if (it.isUpperCase()) {
            it.lowercase(Locale.getDefault())
        } else {
            it.toString()
        }
    }
}
