package com.mospolytech.data.schedule.model.response

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ApiGroup(
    val title: String,
    val course: Int,
    val dateFrom: LocalDate,
    val dateTo: LocalDate,
    val evening: Int,
    val comment: String,
)
