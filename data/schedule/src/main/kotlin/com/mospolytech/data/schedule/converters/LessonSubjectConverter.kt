package com.mospolytech.data.schedule.converters

import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.repository.LessonSubjectsRepository

class LessonSubjectConverter(
    private val lessonSubjectsRepository: LessonSubjectsRepository
) {
    fun convertTitle(title: String): LessonSubjectInfo {
        return lessonSubjectsRepository.add(title)
    }
}