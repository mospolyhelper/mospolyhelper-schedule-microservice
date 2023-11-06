package com.mospolytech.data.peoples.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StaffResponse(
    @SerialName("fio")
    val fio: String,
    @SerialName("division")
    val division: String,
    @SerialName("post")
    val post: String,
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String,
    @SerialName("avatar")
    val avatar: String,
)
