package com.mospolytech.domain.base.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.time.LocalTime

fun LocalDate.atTime(time: LocalTime): LocalDateTime =
    LocalDateTime(year, monthNumber, dayOfMonth, time.hour, time.minute, time.second, time.nano)