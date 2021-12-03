package com.mospolytech.mph.domain.schedule.model

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class Lesson(
    val title: String,
    val type: String,
    val teachers: List<Teacher>,
    val groups: List<Group>,
    val places: List<Place>,
)