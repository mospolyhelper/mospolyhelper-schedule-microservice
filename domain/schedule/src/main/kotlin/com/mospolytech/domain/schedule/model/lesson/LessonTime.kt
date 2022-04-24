package com.mospolytech.domain.schedule.model.lesson

import com.mospolytech.domain.base.utils.converters.LocalTimeConverter
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class LessonTime(
    @Serializable(with = LocalTimeConverter::class)
    val start: LocalTime,
    @Serializable(with = LocalTimeConverter::class)
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