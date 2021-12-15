package com.mospolytech.domain.schedule.model

import com.mospolytech.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalDate

@Serializable
data class LessonTimesReview(
    val lessonTitle: String,
    val days: List<LessonReviewDay>
)
@Serializable
sealed class LessonReviewDay {
    @Serializable
    @SerialName("regular")
    data class Regular(
        val lessonType: String,
        val dayOfWeek: DayOfWeek,
        val time: LessonTime,
        @Serializable(with = LocalDateConverter::class)
        val dateFrom: LocalDate,
        @Serializable(with = LocalDateConverter::class)
        val dateTo: LocalDate
    ) : LessonReviewDay()

    @Serializable
    @SerialName("single")
    data class Single(
        val lessonType: String,
        @Serializable(with = LocalDateConverter::class)
        val date: LocalDate,
        val time: LessonTime
    ) : LessonReviewDay()
}