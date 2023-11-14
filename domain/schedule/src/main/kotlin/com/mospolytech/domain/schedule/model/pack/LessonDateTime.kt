package com.mospolytech.domain.schedule.model.pack

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable

@Serializable
data class LessonDateTime(
    val dateTime: LocalDateTime,
    val timeZone: TimeZone,
)
