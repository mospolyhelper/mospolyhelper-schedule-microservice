package com.mospolytech.domain.base.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime
import java.time.LocalDate as JavaLocalDate
import java.time.LocalTime as JavaLocalTime

val LocalDate.Companion.MIN: LocalDate
    get() = JavaLocalDate.MIN.toKotlinLocalDate()

val LocalDate.Companion.MAX: LocalDate
    get() = JavaLocalDate.MAX.toKotlinLocalDate()

val LocalTime.Companion.MIN: LocalTime
    get() = JavaLocalTime.MIN.toKotlinLocalTime()

val LocalTime.Companion.MAX: LocalTime
    get() = JavaLocalTime.MAX.toKotlinLocalTime()

private val moscowTimeZone = TimeZone.of("Europe/Moscow")
val TimeZone.Companion.Moscow: TimeZone
    get() = moscowTimeZone
