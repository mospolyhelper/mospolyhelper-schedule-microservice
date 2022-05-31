package com.mospolytech.domain.peoples.model

import com.mospolytech.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Teacher(
    val id: String,
    val name: String,
    val avatar: String?,
    val dialogId: String?,
    @Serializable(LocalDateConverter::class)
    val birthday: LocalDate?,
    val grade: String?,
    val department: String?,
    val sex: String?,
    val additionalInfo: String?
)
