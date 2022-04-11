package com.mospolytech.data.schedule.converters

import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo

object LessonSubjectConverter {
    fun convertTitle(title: String): LessonSubjectInfo {
        return LessonSubjectInfo.create(title)
    }
}