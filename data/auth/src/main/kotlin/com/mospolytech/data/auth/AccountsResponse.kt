package com.mospolytech.data.auth

import com.mospolytech.domain.auth.AccountModel
import com.mospolytech.domain.auth.AccountsModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigInteger
import java.security.MessageDigest

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
        val avatar: String,
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

fun AccountsResponse.User.isStudent(): Boolean {
    return userStatus == "stud"
}

fun AccountsResponse.Account.isStudent(): Boolean {
    return userStatus == "stud"
}

fun AccountsResponse.User.isStaff(): Boolean {
    return userStatus == "staff"
}

fun AccountsResponse.Account.isStaff(): Boolean {
    return userStatus == "staff"
}

private fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

fun AccountsResponse.toModel(guid: String?): AccountsModel {
    val fullName = this.user.surname + " " + this.user.name + " " + this.user.patronymic
    val isStudent = user.isStudent()
    val description =
        if (isStudent) {
            "${user.group} • ${user.degreeLevel?.lowercase() ?: "студент"}"
        } else {
            if (user.workPlace == null) {
                "Сотрудник"
            } else {
                "Сотрудник • ${user.workPlace}"
            }
        }

    val id = generateId(guid)
    val current = AccountModel(
        id = id,
        name = fullName,
        description = description,
        avatar = this.user.avatar.ifEmpty { null },
    )

    val accounts = buildList {
        add(current)

        user.accounts?.forEach { account ->
            add(account.toModel(guid))
        }
    }

    return AccountsModel(
        current = current.id,
        accounts = accounts,
    )
}

fun AccountsResponse.Account.toModel(guid: String?): AccountModel {
    val fullName = this.fio
    val jwt = this.jwt ?: this.jwtRefresh
    val guid2 = jwt?.parseJwtGuid() ?: guid
    val id = generateId(guid2)
    val isStudent = isStudent()

    val description =
        if (isStudent) {
            "$group • ${degreeLevel?.lowercase() ?: "студент"}"
        } else {
            if (workPlace == null) {
                "Сотрудник"
            } else {
                "Сотрудник • $workPlace"
            }
        }

    return AccountModel(
        id = id,
        name = fullName,
        description = description,
        avatar = this.avatar,
    )
}

fun AccountsResponse.generateId(guid: String?): String {
    val fullName = this.user.surname + " " + this.user.name + " " + this.user.patronymic
    val id = guid ?: this.user.id
    val userStatus = this.user.status
    val group = this.user.group.orEmpty()
    val workPlace = this.user.workPlace.orEmpty()

    return md5(id + fullName + userStatus + group + workPlace)
}

fun AccountsResponse.Account.generateId(guid: String?): String {
    val fullName = this.fio
    val jwt = this.jwt ?: this.jwtRefresh ?: guid
    val id = jwt?.parseJwtGuid().orEmpty()
    val userStatus = this.status
    val group = this.group.orEmpty()
    val workPlace = this.workPlace.orEmpty()

    return md5(id + fullName + userStatus + group + workPlace)
}
