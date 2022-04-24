package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.schedule.model.teacher.TeacherInfo
import com.mospolytech.domain.schedule.repository.TeachersRepository

class TeachersRepositoryImpl : TeachersRepository {
    private val map = mutableMapOf<String, TeacherInfo>()

    override fun add(name: String, description: String): TeacherInfo {
        return TeacherInfo(
            id = name,
            name = name,
            description = description
        ).run {
            map.getOrPut(id) { this }
        }
    }

    override fun get(id: String): TeacherInfo? {
        return map[id]
    }
}