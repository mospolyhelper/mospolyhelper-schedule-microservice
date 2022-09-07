package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo

interface LessonSubjectsRepository {
    suspend fun add(title: String): String
    suspend fun get(id: String): LessonSubjectInfo?

    suspend fun getAll(): List<LessonSubjectInfo>
}
