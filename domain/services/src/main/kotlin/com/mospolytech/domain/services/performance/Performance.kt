package com.mospolytech.domain.services.performance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Performance(
    @SerialName("id")
    val id: String,
    @SerialName("grades")
    val grades: List<GradePosition>,
)
