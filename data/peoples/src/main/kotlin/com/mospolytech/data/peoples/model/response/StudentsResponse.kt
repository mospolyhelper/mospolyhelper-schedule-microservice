package com.mospolytech.data.peoples.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentsResponse(
    @SerialName("fio")
    val fio: String,
    @SerialName("group")
    val group: String,
    @SerialName("faculty")
    val faculty: String,
    @SerialName("id")
    val id: String,
    @SerialName("avatar")
    val avatar: String,
)
