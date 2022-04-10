package com.mospolytech.data.schedule.converters

import com.mospolytech.domain.schedule.model.teacher.TeacherInfo

object LessonTeachersConverter {
    fun convertTeachers(teachers: String): List<TeacherInfo> {
        return teachers.split(", ").mapNotNull {
            if (it.isEmpty()) {
                null
            } else {
                TeacherInfo.create(
                    name = it,
                    description = "Описание преподавателя"
                )
            }
        }
    }
}