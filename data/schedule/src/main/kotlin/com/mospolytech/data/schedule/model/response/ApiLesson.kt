package com.mospolytech.data.schedule.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiLesson(
    val sbj: String,
    val teacher: String,
    val dts: String = "",
    val df: String = "",
    val dt: String = "",
    val auditories: List<Auditory>,
    val shortRooms: List<String>,
    val location: String,
    val type: String,
    val week: String = "",
    val align: String = "",
    @SerialName("e_link")
    val eLink: String?
) {
    @Serializable
    data class Auditory(
        val title: String,
        val color: String
    )
}
