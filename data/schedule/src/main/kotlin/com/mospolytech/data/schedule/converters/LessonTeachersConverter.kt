package com.mospolytech.data.schedule.converters

import com.mospolytech.domain.schedule.model.Teacher

object LessonTeachersConverter {
    fun convertTeachers(teachers: String): List<Teacher> {
        return teachers.split(", ").map { Teacher(it) }
    }
}