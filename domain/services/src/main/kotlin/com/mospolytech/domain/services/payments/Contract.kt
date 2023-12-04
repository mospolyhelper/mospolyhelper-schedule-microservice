package com.mospolytech.domain.services.payments

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Contract(
    val id: String,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val balance: String,
    val isNegativeBalance: Boolean,
    val paymentMethods: List<PaymentMethod>,
    val payments: List<Payment>,
)
