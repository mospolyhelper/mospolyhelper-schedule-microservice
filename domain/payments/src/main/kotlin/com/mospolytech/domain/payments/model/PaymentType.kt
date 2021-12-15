package com.mospolytech.domain.payments.model

import kotlinx.serialization.Serializable

@Serializable
enum class PaymentType {
    DORMITORY, EDUCATION
}