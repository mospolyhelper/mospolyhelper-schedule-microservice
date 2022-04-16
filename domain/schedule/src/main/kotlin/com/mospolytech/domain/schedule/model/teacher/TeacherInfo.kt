package com.mospolytech.domain.schedule.model.teacher

import kotlinx.serialization.Serializable

@Serializable
data class TeacherInfo(
    val id: String,
    val name: String,
    val description: String
)