package com.mospolytech.domain.schedule.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleComplexFilter(
    val typesId: List<String>,
    val subjectsId: List<String>,
    val teachersId: List<String>,
    val groupsId: List<String>,
    val placesId: List<String>,
)
