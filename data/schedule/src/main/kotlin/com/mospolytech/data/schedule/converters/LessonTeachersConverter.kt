package com.mospolytech.data.schedule.converters

import com.mospolytech.domain.schedule.model.teacher.Teacher
import io.ktor.util.*

object LessonTeachersConverter {
    fun convertTeachers(teachers: String): List<Teacher> {
        return teachers.split(", ").map { Teacher(it.encodeBase64(), it) }
    }
}