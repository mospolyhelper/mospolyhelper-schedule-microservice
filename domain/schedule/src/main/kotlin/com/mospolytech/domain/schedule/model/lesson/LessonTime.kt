package com.mospolytech.domain.schedule.model.lesson

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class LessonTime(
    val start: LocalTime,
    val end: LocalTime
) : Comparable<LessonTime> {
    override fun compareTo(other: LessonTime): Int {
        val start = this.start.compareTo(other.start)
        if (start == 0) {
            val end = this.end.compareTo(other.end)
            return end
        }
        return start
    }
}
