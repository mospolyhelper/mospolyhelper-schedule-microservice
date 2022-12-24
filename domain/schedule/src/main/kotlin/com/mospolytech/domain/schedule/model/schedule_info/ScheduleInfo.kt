package com.mospolytech.domain.schedule.model.schedule_info

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleInfo(
    val type: ScheduleInfoObject,
    val key: String,
)
