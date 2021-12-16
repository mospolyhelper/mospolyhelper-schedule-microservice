package com.mospolytech.domain.personal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EducationType {
    @SerialName("bachelor")
    Bachelor,
    @SerialName("magistrate")
    Magistrate,
    @SerialName("Aspirant")
    Aspirant
}