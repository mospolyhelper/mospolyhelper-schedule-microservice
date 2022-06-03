package com.mospolytech.domain.peoples.model

import com.mospolytech.domain.base.model.EducationType
import com.mospolytech.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Student(
    val id: String,
    val firstName: String,
    val secondName: String,
    val surname: String,
    val sex: String,
    val avatar: String?,
    @Serializable(LocalDateConverter::class)
    val birthday: LocalDate?,
    val faculty: String,
    val direction: String,
    val specialization: String?,
    val educationType: EducationType?,
    val educationForm: EducationForm?,
    val payment: Boolean,
    val course: Int?,
    val group: String?,
    val years: String,
    val dialogId: String?,
    val additionalInfo: String?
) {
    fun getFullName() = "$secondName $firstName $surname"
}
