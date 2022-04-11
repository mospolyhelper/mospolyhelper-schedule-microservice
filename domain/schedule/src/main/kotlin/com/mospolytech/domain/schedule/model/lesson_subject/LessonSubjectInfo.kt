package com.mospolytech.domain.schedule.model.lesson_subject

import io.ktor.util.*
import kotlinx.serialization.Serializable

@Serializable
data class LessonSubjectInfo(
    val id: String,
    val title: String
) {
    companion object {
        val map = mutableMapOf<String, LessonSubjectInfo>()

        fun create(
            title: String
        ) = LessonSubjectInfo(
            id = title,
            title = title
        ).run {
            map.getOrPut(id) { this }
        }
    }
}