package com.mospolytech.domain.peoples.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: String,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val sex: String?,
    val avatar: String?,
    val birthday: LocalDate?,
    val group: Group?,
    val specialization: StudentSpecialization?,
    val educationType: String,
    val educationForm: String,
    val payment: String,
    val course: Int?,
    val years: String,
    var code: String,
    var dormitory: String?,
    var dormitoryRoom: String?,
    var branch: StudentBranch,
)

val Student.description
    get() = buildString {
        group?.let {
            append(it.description)
        }
    }
