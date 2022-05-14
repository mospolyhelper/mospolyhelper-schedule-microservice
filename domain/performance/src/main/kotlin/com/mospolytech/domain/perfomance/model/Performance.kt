package com.mospolytech.domain.perfomance.model

import com.mospolytech.domain.base.utils.converters.LocalDateTimeConverter
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Performance(
    val id : Int,
    val billNum : Int,
    val billType : String,
    val docType : String,
    val name : String,
    @Serializable(with = LocalDateTimeConverter::class)
    val dateTime : LocalDateTime,
    val grade : String,
    val ticketNum : String?,
    val teacher : String,
    val course : Int,
    val examType : String,
    val chair : String
)
