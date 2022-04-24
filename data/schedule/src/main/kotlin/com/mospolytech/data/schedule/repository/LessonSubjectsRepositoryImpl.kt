package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.repository.LessonSubjectsRepository

class LessonSubjectsRepositoryImpl : LessonSubjectsRepository {
    private val map = mutableMapOf<String, LessonSubjectInfo>()

    override fun add(title: String): LessonSubjectInfo {
        return LessonSubjectInfo(
            id = title,
            title = title
        ).run {
            map.getOrPut(id) { this }
        }
    }

    override fun get(id: String): LessonSubjectInfo? {
        return map[id]
    }
}