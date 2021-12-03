package com.mospolytech.mph.domain.schedule.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleSource(
    val type: ScheduleSources,
    val key: String
)