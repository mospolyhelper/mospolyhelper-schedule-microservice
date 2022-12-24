package com.mospolytech.data.schedule.model.response

import com.mospolytech.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ApiGroup(
    val title: String,
    val course: Int,
    @Serializable(with = LocalDateConverter::class)
    val dateFrom: LocalDate,
    @Serializable(with = LocalDateConverter::class)
    val dateTo: LocalDate,
    val evening: Int,
    val comment: String,
)
