package com.mospolytech.domain.base.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

fun BigDecimal.formatRoubles(): String {
    return formatter.format(this) + "₽"
}

private val locale = Locale("ru")
private val formatter = NumberFormat.getCurrencyInstance(locale)
