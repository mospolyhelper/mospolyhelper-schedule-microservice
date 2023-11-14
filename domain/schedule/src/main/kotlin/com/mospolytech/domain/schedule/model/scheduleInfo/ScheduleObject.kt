package com.mospolytech.domain.schedule.model.scheduleInfo

import kotlinx.serialization.Serializable

@Serializable
class ScheduleObject(
    val id: String,
    val title: String,
    val description: String,
    val avatar: String?,
)
