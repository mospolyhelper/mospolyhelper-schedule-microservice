package com.mospolytech.domain.services.payments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContractHeader(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
)
