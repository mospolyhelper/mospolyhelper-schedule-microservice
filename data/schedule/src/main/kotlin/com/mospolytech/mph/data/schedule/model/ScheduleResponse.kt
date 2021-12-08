package com.mospolytech.mph.data.schedule.model

import com.mospolytech.mph.domain.base.utils.converters.LocalDateConverter
import com.mospolytech.mph.domain.base.utils.converters.LocalTimeConverter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class ScheduleResponse(
    @Serializable(with = LocalDateConverter::class)
    val date: LocalDate,
    @Serializable(with = LocalTimeConverter::class)
    val time: LocalTime,
    val v: String,
    val copyright: String,
    val contents: Map<String, ApiSchedule>
)

@Serializable
data class ApiSchedule(
    val grid: Map<String, Map<String, List<ApiLesson>>>,
    val group: ApiGroup,
    val isSession: Boolean
)

@Serializable
data class ApiLesson(
    val sbj: String,
    val teacher: String,
    val dts: String = "",
    val df: String = "",
    val dt: String = "",
    val auditories: List<Auditory>,
    val type: String,
    val week: String = "",
    val align: String = "",
    @SerialName("e_link")
    val eLink: String?
) {
    @Serializable
    data class Auditory(
        val title: String,
        val color: String
    )
}

@Serializable
data class ApiGroup(
    val title: String,
    val course: Int,
    @Serializable(with = LocalDateConverter::class)
    val dateFrom: LocalDate,
    @Serializable(with = LocalDateConverter::class)
    val dateTo: LocalDate,
    val evening: Int,
    val comment: String
)