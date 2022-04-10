package com.mospolytech.domain.schedule.model.schedule

import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import kotlinx.serialization.Serializable

@Serializable
data class SchedulePack(
    val lessons: List<LessonDateTimes>,
    val info: ScheduleInfo
)
