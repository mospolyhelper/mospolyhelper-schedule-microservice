package com.mospolytech.domain.personal.model

import com.mospolytech.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Personal(
    val id: Int,
    val userStatus: String,
    val status: String,
    val course: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val avatar: String,
    val birthday: String,
    val sex: String,
    val code: String,
    val faculty: String,
    val group: String,
    val specialty: String,
    val specialization: String?,
    val degreeLength: String,
    val educationForm: String,
    val finance: String,
    val degreeLevel: String,
    val enterYear: String,
    val orders: List<Order>,
    val subdivisions: List<Subdivision>? = null,
)

@Serializable
data class Subdivision(
    val category: String,
    val jobType: String? = null,
    val status: String? = null,
    val subdivision: String? = null,
    val wage: String? = null,
)

@Serializable
data class Order(
    @Serializable(LocalDateConverter::class)
    val date: LocalDate?,
    val name: String,
    val description: String,
)
