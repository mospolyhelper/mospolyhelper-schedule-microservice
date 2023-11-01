package com.mospolytech.data.peoples.repository

import com.mospolytech.data.peoples.model.xml.toModel
import com.mospolytech.data.peoples.remote.StudentsRemoteDS
import com.mospolytech.data.peoples.service.StudentsService
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.StudentShort
import com.mospolytech.domain.peoples.repository.StudentsRepository
import com.mospolytech.domain.personal.repository.PersonalRepository

class StudentsRepositoryImpl(
    private val studentsService: StudentsService,
    private val studentsDS: StudentsRemoteDS,
    private val personalRepository: PersonalRepository,
) : StudentsRepository {

    override suspend fun getStudents(query: String, page: Int, pageSize: Int) =
        studentsDS.getStudentsPaging(query, pageSize, page)

    override suspend fun getShortStudents(query: String, page: Int, pageSize: Int): PagingDTO<StudentShort> {
        return studentsDS.getShortStudents(query, pageSize, page)
    }

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
            studentsDS.clearData()
        }

        val studentsFile = studentsService.downloadStudents()
        try {
            val students = studentsService.parseStudents(studentsFile).map { it.toModel() }
            studentsDS.addStudents(students)
        } finally {
            studentsFile.delete()
        }
    }
}
