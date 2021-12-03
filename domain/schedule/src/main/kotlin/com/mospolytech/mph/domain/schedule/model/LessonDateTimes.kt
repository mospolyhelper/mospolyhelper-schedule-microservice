package com.mospolytech.mph.domain.schedule.model

import kotlinx.serialization.Serializable
import com.mospolytech.mph.domain.base.utils.converters.LocalDateTimeConverter
import java.time.LocalDateTime

@Serializable
data class LessonDateTimes(
    val lesson: Lesson,
    val time: List<LessonDateTime>
)

@Serializable
data class LessonDateTime(
    @Serializable(with = LocalDateTimeConverter::class)
    val start: LocalDateTime,
    @Serializable(with = LocalDateTimeConverter::class)
    val end: LocalDateTime
)