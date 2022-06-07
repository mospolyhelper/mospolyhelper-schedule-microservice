package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.peoples.repository.PeoplesRepository
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.schedule.repository.TeachersRepository

class TeachersRepositoryImpl(
    private val peoplesRepository: PeoplesRepository
) : TeachersRepository {
    private val map = mutableMapOf<String, String>()

    override suspend fun findAndGetId(name: String): String {
        val id = map[name]
        val teacher = id?.let { map[it] }

        if (teacher != null) return teacher


        val teacher2 = peoplesRepository.getTeacher(name).getOrNull()

        val id2 = teacher2?.id ?: name

        return map.getOrPut(id2) {
            id2
        }
    }

    override fun get(id: String): Teacher? {
        return map[id] as? Teacher
    }
}