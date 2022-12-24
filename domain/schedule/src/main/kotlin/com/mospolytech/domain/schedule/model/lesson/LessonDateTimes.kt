package com.mospolytech.domain.schedule.model.lesson

import kotlinx.datetime.*
import kotlinx.serialization.Serializable

@Serializable
data class LessonDateTime(
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val time: LessonTime,
)

fun LessonDateTime.toDateTimeRanges(): List<ClosedRange<LocalDateTime>> {
    return if (endDate == null) {

        listOf(startDate.atTime(time.start)..startDate.atTime(time.end))
    } else {
        generateDatesFromRange(startDate, endDate).map {
            it.atTime(time.start)..it.atTime(time.end)
        }
    }
}

private fun generateDatesFromRange(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    var currentDay = startDate
    do {
        dates += currentDay
        currentDay = currentDay.plus(DatePeriod(days = 7))
    } while (currentDay <= endDate)
    return dates
}
