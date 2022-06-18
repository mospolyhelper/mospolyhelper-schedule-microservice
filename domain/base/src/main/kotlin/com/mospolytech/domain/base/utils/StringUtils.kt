package com.mospolytech.domain.base.utils

/**
 * Returns this char sequence if it's empty
 * or the result of calling [defaultValue] function if the char sequence is not empty.
 */
@SinceKotlin("1.3")
inline fun <C : R, R : CharSequence> C.ifNotEmpty(defaultValue: () -> R): R =
    if (isEmpty()) this else defaultValue()