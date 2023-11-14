package com.mospolytech.domain.schedule.model.lessonType

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Importance {
    @SerialName("low")
    Low,

    @SerialName("normal")
    Normal,

    @SerialName("high")
    High,
}
