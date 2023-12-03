package com.mospolytech.domain.schedule.model.source

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ScheduleSourceTypes {
    @SerialName("group")
    Group,

    @SerialName("teacher")
    Teacher,

    @SerialName("student")
    Student,

    @SerialName("place")
    Place,

    @SerialName("subject")
    Subject,

    @SerialName("complex")
    Complex,
}
