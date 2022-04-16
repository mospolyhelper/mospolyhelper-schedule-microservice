package com.mospolytech.domain.schedule.model.group

import kotlinx.serialization.Serializable

@Serializable
data class GroupInfo(
    val id: String,
    val title: String,
    val description: String,
    val course: String,
    val isEvening: Boolean
)