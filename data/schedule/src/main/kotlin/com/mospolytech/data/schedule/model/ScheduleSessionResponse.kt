package com.mospolytech.data.schedule.model

import com.mospolytech.domain.base.utils.converters.LocalDateConverter
import com.mospolytech.domain.base.utils.converters.LocalTimeConverter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class ScheduleSessionResponse(
    @Serializable(with = LocalDateConverter::class)
    val date: LocalDate,
    @Serializable(with = LocalTimeConverter::class)
    val time: LocalTime,
    val v: String,
    val copyright: String,
    val contents: List<ApiSchedule>
)