package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo

interface LessonSubjectsRepository {
    fun add(title: String): LessonSubjectInfo
    fun get(id: String): LessonSubjectInfo?
}