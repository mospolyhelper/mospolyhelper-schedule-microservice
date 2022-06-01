package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.peoples.repository.PeoplesRepository
import com.mospolytech.domain.schedule.model.teacher.TeacherInfo
import com.mospolytech.domain.schedule.repository.TeachersRepository

class TeachersRepositoryImpl(
    private val peoplesRepository: PeoplesRepository
) : TeachersRepository {
    private val map = mutableMapOf<String, TeacherInfo>()
    private val map2 = mutableMapOf<String, String>()

    override fun add(name: String, description: String): TeacherInfo {
        val rawId = name
        val id = map2.get(rawId)
        val teacher = id?.let { map.get(it) }

        if (teacher != null) return teacher


        val teacher2 = peoplesRepository.getTeachers()
            .firstOrNull { it.name == name }

        val id2 = teacher2?.id ?: rawId

        val description = teacher2
            ?.let { it.department?.title ?: it.departmentParent?.title } ?: ""


        return map.getOrPut(id2) {
            TeacherInfo(
                id = id2,
                name = name,
                description = description
            )
        }
    }

    override fun get(id: String): TeacherInfo? {
        return map[id]
    }
}