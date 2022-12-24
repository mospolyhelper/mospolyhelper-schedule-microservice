package com.mospolytech.domain.payments.model

import kotlinx.serialization.Serializable

@Serializable
data class Contracts(
    val contracts: Map<PaymentType, Payments>,
)
