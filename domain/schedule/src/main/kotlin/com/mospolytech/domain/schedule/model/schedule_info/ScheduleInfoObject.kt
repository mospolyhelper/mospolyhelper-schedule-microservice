package com.mospolytech.domain.schedule.model.schedule_info

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ScheduleInfoObject {
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
    @SerialName("lesson-type")
    LessonType
}
