package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo

interface LessonTypesRepository {
    suspend fun add(
        title: String,
        shortTitle: String,
        description: String,
        isImportant: Boolean
    ): String
    suspend fun get(id: String): LessonTypeInfo?
    suspend fun getAll(): List<LessonTypeInfo>
}
