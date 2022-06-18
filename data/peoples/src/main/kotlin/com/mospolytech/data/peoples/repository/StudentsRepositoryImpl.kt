package com.mospolytech.data.peoples.repository

import com.mospolytech.data.peoples.model.xml.toModel
import com.mospolytech.data.peoples.remote.StudentsRemoteDS
import com.mospolytech.data.peoples.service.StudentsService
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.repository.StudentsRepository
import com.mospolytech.domain.personal.repository.PersonalRepository

class StudentsRepositoryImpl(
    studentsService: StudentsService,
    private val studentsDS: StudentsRemoteDS,
    private val personalRepository: PersonalRepository
): StudentsRepository {

    private val studentsLocalCache by lazy {
        studentsService.getStudents()
            .map { it.toModel() }
            .distinctBy { it.id }
            .toList()
    }

    override suspend fun getStudents(name: String, page: Int, pageSize: Int) =
        studentsDS.getStudentsPaging(name, pageSize, page)

    override suspend fun getStudents() = studentsDS.getStudents()

    override suspend fun getShortStudents() = studentsDS.getShortStudents()

    override suspend fun getClassmates(token: String): Result<List<Student>> {
        return personalRepository.getPersonalInfo(token).mapCatching {
            studentsDS.getStudents(it.group)
        }
    }

    override suspend fun updateData(recreateDb: Boolean) {
        if (recreateDb) {
            studentsDS.deleteTables()
            studentsDS.createTables()
        } else {
            //studentsDS.clearData()
        }
        studentsDS.addStudents(studentsLocalCache)
    }

}
