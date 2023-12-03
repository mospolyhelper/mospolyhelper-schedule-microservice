package com.mospolytech.domain.schedule.model.place

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class PlaceOccupancyTimeRange(
    val timeFrom: LocalTime,
    val timeTo: LocalTime,
    val value: Double,
)
