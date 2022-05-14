package com.mospolytech.domain.payments.model

import kotlinx.serialization.Serializable

@Serializable
data class Payment (
	val date : String,
	val value : String
)