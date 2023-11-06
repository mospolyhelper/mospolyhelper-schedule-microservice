package com.mospolytech.domain.peoples.model

import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: String,
    val name: String,
    val avatar: String?,
    val faculty: String?,
    val direction: String?,
    val group: Group?,
    val specialization: String?,
    val educationType: String?,
    val educationForm: String?,
    val payment: String?,
    val course: Int?,
    val years: String?,
    val code: String?,
    val branch: String?,
)
