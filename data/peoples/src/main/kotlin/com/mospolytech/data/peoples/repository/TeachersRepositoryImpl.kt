package com.mospolytech.data.peoples.repository

import com.mospolytech.data.peoples.model.xml.toModel
import com.mospolytech.data.peoples.remote.StudentsRemoteDS
import com.mospolytech.data.peoples.remote.TeachersRemoteDS
import com.mospolytech.data.peoples.service.StudentsService
import com.mospolytech.data.peoples.service.TeachersService
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.peoples.repository.StudentsRepository
import com.mospolytech.domain.peoples.repository.TeachersRepository
import com.mospolytech.domain.personal.repository.PersonalRepository

class TeachersRepositoryImpl(
    teachersService: TeachersService,
    private val teachersDS: TeachersRemoteDS
): TeachersRepository {

    private val teachersLocalCache by lazy {
        teachersService.getTeachers()
            .map { it.toModel() }
            .distinctBy { it.id }
            .toList()
    }

    override suspend fun getTeachers(name: String, page: Int, pageSize: Int) =
        teachersDS.getTeachersPaging(name, pageSize, page)

    override suspend fun getTeachers() = teachersDS.getTeachers()

    override suspend fun getTeacher(name: String): Result<Teacher?> {
        return kotlin.runCatching {
            teachersDS.getTeacher(name)
        }
    }


    override suspend fun updateData() {
        teachersDS.clearData()
        teachersLocalCache.forEach {
            teachersDS.addTeacher(it)
        }
    }

}
