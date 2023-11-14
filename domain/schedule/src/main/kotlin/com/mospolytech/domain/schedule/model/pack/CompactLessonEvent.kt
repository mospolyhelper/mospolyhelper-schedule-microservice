package com.mospolytech.domain.schedule.model.pack

import com.mospolytech.domain.schedule.model.lessonType.Importance
import kotlinx.serialization.Serializable

@Serializable
data class CompactLessonEvent(
    val id: String,
    val tags: List<String>,
    val subjectId: String,
    val attendeesId: List<String>,
    val placesId: List<String>,
    val start: LessonDateTime,
    val end: LessonDateTime,
    val recurrence: List<String>,
    val importance: Importance,
)

// fun CompactLessonEvent.toDateTimeRanges(): List<ClosedRange<LocalDateTime>> {
//    return if (endDate == null) {
//
//        listOf(startDate.atTime(time.start)..startDate.atTime(time.end))
//    } else {
//        generateDatesFromRange(startDate, endDate).map {
//            it.atTime(time.start)..it.atTime(time.end)
//        }
//    }
// }
//
// private fun generateDatesFromRange(
//    startDate: LocalDate,
//    endDate: LocalDate,
// ): List<LocalDate> {
//    val dates = mutableListOf<LocalDate>()
//    var currentDay = startDate
//    do {
//        dates += currentDay
//        currentDay = currentDay.plus(DatePeriod(days = 7))
//    } while (currentDay <= endDate)
//    return dates
// }
