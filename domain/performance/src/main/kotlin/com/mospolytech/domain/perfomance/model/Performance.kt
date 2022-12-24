package com.mospolytech.domain.perfomance.model

import com.mospolytech.domain.base.utils.converters.LocalDateConverter
import com.mospolytech.domain.base.utils.converters.LocalTimeConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class Performance(
    val id: Int,
    val billNum: String,
    val billType: String?,
    val docType: String,
    val name: String,
    @Serializable(with = LocalDateConverter::class)
    val date: LocalDate?,
    @Serializable(with = LocalTimeConverter::class)
    val time: LocalTime?,
    val grade: String,
    val ticketNum: String?,
    val teacher: String,
    val course: Int,
    val semester: Int,
    val examType: String,
    val chair: String,
)
