package com.mospolytech.domain.payments.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethod(
    val type: String,
    val url: String,
    val info: String,
) {
    companion object {
        const val URL_TYPE = "url"
    }
}
