package com.mospolytech.domain.base.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EducationType {
    @SerialName("bachelor")
    Bachelor,
    @SerialName("magistrate")
    Magistrate,
    @SerialName("aspirant")
    Aspirant,
    @SerialName("college")
    College
}