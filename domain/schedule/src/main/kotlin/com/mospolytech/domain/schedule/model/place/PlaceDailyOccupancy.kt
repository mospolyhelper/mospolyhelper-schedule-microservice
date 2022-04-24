package com.mospolytech.domain.schedule.model.place

import com.mospolytech.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class PlaceDailyOccupancy(
    @Serializable(with = LocalDateConverter::class)
    val date: LocalDate,
    val values: List<PlaceOccupancyTimeRange>
)