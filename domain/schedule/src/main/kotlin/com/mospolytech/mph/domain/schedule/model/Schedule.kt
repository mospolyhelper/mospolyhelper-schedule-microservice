package com.mospolytech.mph.domain.schedule.model

import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val lessons: List<ScheduleDay>
)