package com.mospolytech.domain.perfomance.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformancePeriod(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
)
