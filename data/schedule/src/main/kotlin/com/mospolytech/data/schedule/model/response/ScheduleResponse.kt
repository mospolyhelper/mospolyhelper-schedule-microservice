package com.mospolytech.data.schedule.model.response

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    val date: LocalDate,
    val time: LocalTime,
    val v: String,
    val copyright: String,
    val contents: Map<String, ApiSchedule>,
)
