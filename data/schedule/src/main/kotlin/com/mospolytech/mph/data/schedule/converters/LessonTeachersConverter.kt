package com.mospolytech.mph.data.schedule.converters

import com.mospolytech.mph.domain.schedule.model.Teacher

object LessonTeachersConverter {
    fun convertTeachers(teachers: String): List<Teacher> {
        return teachers.split(", ").map { Teacher(it) }
    }
}