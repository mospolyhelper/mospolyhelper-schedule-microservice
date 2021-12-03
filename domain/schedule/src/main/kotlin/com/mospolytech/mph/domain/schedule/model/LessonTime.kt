package com.mospolytech.mph.domain.schedule.model

import java.time.LocalTime

data class LessonTime(
    val start: LocalTime,
    val end: LocalTime
)