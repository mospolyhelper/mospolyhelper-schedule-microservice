package com.mospolytech.domain.schedule.model.lesson_subject

import kotlinx.serialization.Serializable

@Serializable
data class LessonSubjectInfo(
    val id: String,
    val title: String
)