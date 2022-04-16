package com.mospolytech.domain.schedule.model.schedule

import com.mospolytech.domain.schedule.model.lesson.Lesson
import com.mospolytech.domain.schedule.model.lesson.LessonTime
import kotlinx.serialization.Serializable

@Serializable
data class LessonsByTime(
    val time: LessonTime,
    val lessons: List<Lesson>
)