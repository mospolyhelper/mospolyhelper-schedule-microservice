package com.mospolytech.data.schedule.converters

import com.mospolytech.domain.schedule.repository.TeachersRepository

class LessonTeachersConverter(
    private val teachersRepository: TeachersRepository
) {
    suspend fun convertTeachers(teachers: String): List<String> {
        return teachers.split(", ").mapNotNull {
            if (it.isEmpty()) {
                null
            } else {
                teachersRepository.findAndGetId(
                    name = it
                )
            }
        }
    }
}