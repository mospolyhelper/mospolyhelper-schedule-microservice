package com.mospolytech.data.peoples.repository

import com.mospolytech.data.peoples.model.xml.toModel
import com.mospolytech.data.peoples.remote.StudentsRemoteDS
import com.mospolytech.data.peoples.remote.TeachersRemoteDS
import com.mospolytech.data.peoples.service.StudentsService
import com.mospolytech.data.peoples.service.TeachersService
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.peoples.repository.PeoplesRepository
import com.mospolytech.domain.personal.repository.PersonalRepository

class PeoplesRepositoryImpl(
    studentsService: StudentsService,
    teachersService: TeachersService,
    private val studentsDS: StudentsRemoteDS,
    private val teachersDS: TeachersRemoteDS,
    private val personalRepository: PersonalRepository
): PeoplesRepository {

    private val teachersLocalCache by lazy {
        teachersService.getTeachers()
            .asSequence()
            .map { it.toModel() }
            .distinctBy { it.id }
            .toList()
    }

    private val studentsLocalCache by lazy {
        studentsService.getStudents()
            .asSequence()
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

    override suspend fun getStudents(name: String, page: Int, pageSize: Int) =
        studentsDS.getStudentsPaging(name, pageSize, page)

    override suspend fun getStudents() = studentsDS.getStudents()

    override suspend fun getClassmates(token: String): Result<List<Student>> {
        return personalRepository.getPersonalInfo(token).mapCatching {
            studentsDS.getStudents(it.group)
        }
    }

    override suspend fun updateData() {
        studentsDS.clearData()
        teachersLocalCache.forEach(teachersDS::addTeacher)
        studentsLocalCache.forEach(studentsDS::addStudent)
    }

}
