package com.mospolytech.domain.schedule.model.lesson_type

import kotlinx.serialization.Serializable

@Serializable
data class LessonType(
    val id: String,
    val title: String
) {
    companion object {
        private val lessonTypeMap = mutableMapOf<LessonTypeInfo, LessonType>()

        fun from(info: LessonTypeInfo) =
            lessonTypeMap.getOrPut(info) {
                LessonType(
                    id = info.id,
                    title = info.shortTitle
                )
            }

    }
}