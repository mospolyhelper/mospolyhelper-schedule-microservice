package com.mospolytech.domain.peoples.model

import com.mospolytech.domain.base.model.Department
import com.mospolytech.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Teacher(
    val id: String,
    val name: String,
    val avatar: String?,
    val stuffType: String?,
    val grade: String?,
    val departmentParent: Department?,
    val department: Department?,
    val email: String?,
    val sex: String?,
    @Serializable(LocalDateConverter::class)
    val birthday: LocalDate?,
    val dialogId: String?,
    val additionalInfo: String? = null
)
