package com.mospolytech.domain.schedule.model.lesson

import com.mospolytech.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class LessonDateTimes(
    val lesson: Lesson,
    val time: List<LessonDateTime>
): Comparable<LessonDateTimes> {
    override fun compareTo(other: LessonDateTimes): Int {
        return lesson.compareTo(other.lesson)
    }
}

@Serializable
data class LessonDateTime(
    @Serializable(with = LocalDateConverter::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateConverter::class)
    val endDate: LocalDate?,
    val time: LessonTime
)

fun LessonDateTime.toDateTimeRanges(): List<ClosedRange<LocalDateTime>> {
    return if (endDate == null) {
        listOf(LocalDateTime.of(startDate, time.start)..LocalDateTime.of(startDate, time.end))
    } else {
        generateDatesFromRange(startDate, endDate).map {
            LocalDateTime.of(it, time.start)..LocalDateTime.of(it, time.end)
        }
    }
}

private fun generateDatesFromRange(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    var currentDay = startDate
    do {
        dates += currentDay
        currentDay = currentDay.plusDays(7)
    } while (currentDay <= endDate)
    return dates
}