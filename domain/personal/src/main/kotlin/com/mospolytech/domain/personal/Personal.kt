package com.mospolytech.domain.personal

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Personal(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("avatar")
    val avatar: String?,
    @SerialName("data")
    val data: List<PersonalData>,
)

@Serializable
data class PersonalData(
    @SerialName("title")
    val title: String,
    @SerialName("value")
    val value: String,
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
    val date: LocalDate?,
    val name: String,
    val description: String,
)
