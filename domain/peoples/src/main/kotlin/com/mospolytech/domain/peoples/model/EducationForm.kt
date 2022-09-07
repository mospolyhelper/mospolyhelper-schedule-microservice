package com.mospolytech.domain.peoples.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EducationForm(val type: String) {
    @SerialName("full_time")
    FullTime("Очно-заочная"),
    @SerialName("evening")
    Evening("Очно-заочная"),
    @SerialName("correspondence")
    Correspondence("Заочная")
}
