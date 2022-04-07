package com.mospolytech.domain.schedule.model.lesson_type

import io.ktor.util.*
import kotlinx.serialization.Serializable

@Serializable
data class LessonTypeInfo(
    val id: String,
    val title: String,
    val shortTitle: String,
    val description: String,
    val isImportant: Boolean
) {
    companion object {
        private val lessonTypeInfoMap = mutableMapOf<Int, LessonTypeInfo>()

        fun create(
            title: String,
            shortTitle: String,
            description: String,
            isImportant: Boolean
        ) = LessonTypeInfo(
            id = title.encodeBase64(),
            title = title,
            shortTitle = shortTitle,
            description = description,
            isImportant = isImportant
        ).apply {
            lessonTypeInfoMap[hashCode()] = this
        }
    }
}