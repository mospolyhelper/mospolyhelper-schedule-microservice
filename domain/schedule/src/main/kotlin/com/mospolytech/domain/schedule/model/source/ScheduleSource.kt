package com.mospolytech.domain.schedule.model.source

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleSource(
    val type: ScheduleSources,
    val key: String,
)
