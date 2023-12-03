package com.mospolytech.domain.payments.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    val id: String,
    val title: String,
    val description: String,
    val date: LocalDate,
    val value: String,
    val isNegative: Boolean,
)
