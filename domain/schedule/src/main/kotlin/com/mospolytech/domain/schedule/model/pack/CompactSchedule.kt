package com.mospolytech.domain.schedule.model.pack

import kotlinx.serialization.Serializable

@Serializable
data class CompactSchedule(
    val lessons: List<CompactLessonAndTimes>,
    val info: ScheduleInfo
)
