package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.repository.LessonTypesRepository

class LessonTypesRepositoryImpl : LessonTypesRepository {
    private val map = mutableMapOf<String, LessonTypeInfo>()

    override fun add(
        title: String,
        shortTitle: String,
        description: String,
        isImportant: Boolean
    ): LessonTypeInfo {
        return LessonTypeInfo(
            id = title,
            title = title,
            shortTitle = shortTitle,
            description = description,
            isImportant = isImportant
        ).run {
            map.getOrPut(id) { this }
        }
    }

    override fun get(id: String): LessonTypeInfo? {
        return map[id]
    }
}