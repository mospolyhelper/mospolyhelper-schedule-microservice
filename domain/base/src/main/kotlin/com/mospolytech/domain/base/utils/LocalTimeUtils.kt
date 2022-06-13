package com.mospolytech.domain.base.utils

import kotlinx.datetime.LocalDateTime
import java.time.LocalTime

val LocalDateTime.time: LocalTime
    get() = LocalTime.of(
        hour,
        minute,
        second,
        nanosecond
    )