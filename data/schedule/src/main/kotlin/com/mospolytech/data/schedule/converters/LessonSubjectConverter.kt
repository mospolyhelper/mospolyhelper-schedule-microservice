package com.mospolytech.data.schedule.converters

import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.repository.LessonSubjectsRepository

class LessonSubjectConverter(
    private val lessonSubjectsRepository: LessonSubjectsRepository
) {
    suspend fun convertTitle(title: String): String {
        return lessonSubjectsRepository.add(title)
    }
}