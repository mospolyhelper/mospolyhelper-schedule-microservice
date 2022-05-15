package com.mospolytech.domain.payments.model

import com.mospolytech.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Payment (
	@Serializable(LocalDateConverter::class)
	val date : LocalDate,
	val value : String
)