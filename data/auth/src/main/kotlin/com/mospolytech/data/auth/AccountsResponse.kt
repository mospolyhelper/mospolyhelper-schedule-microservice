package com.mospolytech.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountsResponse(
    val user: User,
) {
    @Serializable
    data class User(
        val id: String,
        @SerialName("user_status")
        val userStatus: String,
        val name: String,
        val surname: String,
        val patronymic: String,
        val avatar: String,
        val birthday: String,
        val sex: String,
        val code: String,
        val faculty: String,
        val specialty: String,
        val specialization: String,
        val degreeLength: String,
        val enterYear: String,
        val accounts: List<Account>? = null,
        // Student
        @SerialName("status")
        val status: String? = null,
        @SerialName("group")
        val group: String? = null,
        @SerialName("course")
        val course: String? = null,
        @SerialName("educationForm")
        val educationForm: String? = null,
        @SerialName("finance")
        val finance: String? = null,
        @SerialName("degreeLevel")
        val degreeLevel: String? = null,
        // Staff
        @SerialName("work_place")
        val workPlace: String? = null,
        @SerialName("email_staff")
        val emailStaff: String? = null,
    )

    @Serializable
    class Account(
        @SerialName("user_status")
        val userStatus: String,
        @SerialName("fio")
        val fio: String,
        @SerialName("avatar")
        val avatar: String? = null,
        // Student
        @SerialName("status")
        val status: String? = null,
        @SerialName("group")
        val group: String? = null,
        @SerialName("course")
        val course: String? = null,
        @SerialName("educationForm")
        val educationForm: String? = null,
        @SerialName("finance")
        val finance: String? = null,
        @SerialName("degreeLevel")
        val degreeLevel: String? = null,
        // Staff
        @SerialName("work_place")
        val workPlace: String? = null,
        @SerialName("email_staff")
        val emailStaff: String? = null,
        // Token
        @SerialName("token")
        val token: String,
        @SerialName("jwt")
        val jwt: String? = null,
        @SerialName("jwt_refresh")
        val jwtRefresh: String? = null,
    )
}
