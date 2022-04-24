package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo

interface LessonTypesRepository {
    fun add(
        title: String,
        shortTitle: String,
        description: String,
        isImportant: Boolean
    ): LessonTypeInfo
    fun get(id: String): LessonTypeInfo?
}