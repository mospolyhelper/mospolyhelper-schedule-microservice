package com.mospolytech.domain.schedule.model.lessonType

import kotlinx.serialization.Serializable

@Serializable
data class LessonTypeInfo(
    val id: String,
    val title: String,
    val shortTitle: String,
    val description: String?,
)
