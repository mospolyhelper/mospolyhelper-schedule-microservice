package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo

interface LessonTypesRepository {
    suspend fun get(id: String): LessonTypeInfo?

    suspend fun getAll(): List<LessonTypeInfo>
}
