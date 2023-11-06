package com.mospolytech.domain.schedule.model.scheduleInfo

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleInfo(
    val type: ScheduleInfoObject,
    val key: String,
)
