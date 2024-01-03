package com.mospolytech.domain.services.performance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformanceApi(
    @SerialName("periods")
    val periods: List<PerformancePeriod>,
    @SerialName("selected")
    val selected: List<Performance>,
)
