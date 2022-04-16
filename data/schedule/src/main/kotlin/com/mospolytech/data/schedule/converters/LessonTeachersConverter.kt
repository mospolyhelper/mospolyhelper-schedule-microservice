package com.mospolytech.data.schedule.converters

import com.mospolytech.domain.schedule.model.teacher.TeacherInfo
import com.mospolytech.domain.schedule.repository.TeachersRepository

class LessonTeachersConverter(
    private val teachersRepository: TeachersRepository
) {
    fun convertTeachers(teachers: String): List<TeacherInfo> {
        return teachers.split(", ").mapNotNull {
            if (it.isEmpty()) {
                null
            } else {
                teachersRepository.add(
                    name = it,
                    description = "Описание преподавателя"
                )
            }
        }
    }
}