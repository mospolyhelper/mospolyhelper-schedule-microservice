package com.mospolytech.domain.schedule.model.place

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PlaceFilters(
    val ids: List<String>,
    val dateTimeFrom: LocalDateTime,
    val dateTimeTo: LocalDateTime
)
