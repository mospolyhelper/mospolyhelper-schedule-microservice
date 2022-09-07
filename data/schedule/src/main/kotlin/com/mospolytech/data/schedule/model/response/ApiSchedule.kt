package com.mospolytech.data.schedule.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ApiSchedule(
    val grid: Map<String, Map<String, List<ApiLesson>>>,
    val group: ApiGroup,
    val isSession: Boolean
)
