package com.mospolytech.data.peoples.repository

import com.mospolytech.data.peoples.model.xml.toModel
import com.mospolytech.data.peoples.remote.TeachersRemoteDS
import com.mospolytech.data.peoples.service.TeachersService
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.peoples.repository.TeachersRepository

class TeachersRepositoryImpl(
    private val teachersService: TeachersService,
    private val teachersDS: TeachersRemoteDS,
) : TeachersRepository {

    override suspend fun getTeachers(name: String, page: Int, pageSize: Int) =
        teachersDS.getTeachersPaging(name, pageSize, page)

    override suspend fun getTeachers() = teachersDS.getTeachers()

    override suspend fun getTeacher(name: String): Result<Teacher?> {
        return kotlin.runCatching {
            teachersDS.getTeacher(name)
        }
    }

    override suspend fun updateData(recreateDb: Boolean) {
        if (recreateDb) {
            teachersDS.deleteTables()
            teachersDS.createTables()
        } else {
            teachersDS.clearData()
        }

        val teachersFile = teachersService.downloadTeachers()
        val teachers = teachersService.parseTeachers(teachersFile).map { it.toModel() }
        teachersDS.addTeachers(teachers)
    }
}
