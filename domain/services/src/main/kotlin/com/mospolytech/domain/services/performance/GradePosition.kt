package com.mospolytech.domain.services.performance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GradePosition(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String,
    @SerialName("description")
    val description: String,
    @SerialName("grade")
    val grade: Grade?,
)

@Serializable
data class Grade(
    @SerialName("value")
    val value: Int,
    @SerialName("maxValue")
    val maxValue: Int,
    @SerialName("minValue")
    val minValue: Int,
    @SerialName("normalizedValue")
    val normalizedValue: GradeValue?,
    @SerialName("description")
    val description: String,
)

@Serializable
enum class GradeValue {
    @SerialName("very_good")
    VERY_GOOD,

    @SerialName("good")
    GOOD,

    @SerialName("normal")
    NORMAL,

    @SerialName("bad")
    BAD,

    @SerialName("very_bad")
    VERY_BAD,
}
