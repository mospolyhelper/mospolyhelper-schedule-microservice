package com.mospolytech.domain.schedule.model.source

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleSourceType(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
)
