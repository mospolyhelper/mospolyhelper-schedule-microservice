package com.mospolytech.domain.schedule.model.place

import com.mospolytech.domain.base.utils.converters.LocalTimeConverter
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class PlaceOccupancyTimeRange(
    @Serializable(with = LocalTimeConverter::class)
    val timeFrom: LocalTime,
    @Serializable(with = LocalTimeConverter::class)
    val timeTo: LocalTime,
    val value: Double
)
