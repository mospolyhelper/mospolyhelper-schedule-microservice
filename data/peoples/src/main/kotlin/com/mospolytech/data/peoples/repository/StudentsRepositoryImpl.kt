package com.mospolytech.data.peoples.repository

import com.mospolytech.data.base.retryIO
import com.mospolytech.data.peoples.model.response.StudentsResponse
import com.mospolytech.data.peoples.remote.StudentsRemoteDS
import com.mospolytech.data.peoples.service.StudentsService
import com.mospolytech.domain.auth.AuthRepository
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.StudentShort
import com.mospolytech.domain.peoples.repository.StudentsRepository
import com.mospolytech.domain.personal.repository.PersonalRepository
import kotlinx.coroutines.delay

class StudentsRepositoryImpl(
    private val studentsService: StudentsService,
    private val studentsDS: StudentsRemoteDS,
    private val personalRepository: PersonalRepository,
    private val authRepository: AuthRepository,
) : StudentsRepository {
    override suspend fun getStudents(
        query: String,
        page: Int,
        pageSize: Int,
    ) = studentsDS.getStudentsPaging(query, pageSize, page)

    override suspend fun getShortStudents(
        query: String,
        page: Int,
        pageSize: Int,
    ): PagingDTO<StudentShort> {
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
            studentsDS.ensureCreated()
            studentsDS.clearData()
        }

        val studentsFile = studentsService.downloadStudents()
        try {
            val students = studentsService.parseStudents(studentsFile)
            studentsDS.addStudents(students)
        } finally {
            studentsFile.delete()
        }
    }

    suspend fun updateData2(recreateDb: Boolean) {
        if (recreateDb) {
            studentsDS.deleteTables()
            studentsDS.createTables()
        } else {
            studentsDS.ensureCreated()
            studentsDS.clearData()
        }

        val students = getAllStudentsLk()
        studentsDS.addStudents(students)
    }

    private suspend fun getAllStudentsLk(): List<StudentsResponse> {
        val token = authRepository.getTokenForMainUser()

        val resList = mutableListOf<StudentsResponse>()
        val pageSize = 1000
        var currentPage = 1

        while (true) {
            val response =
                retryIO {
                    studentsService.getStudentsLk(
                        token = token,
                        page = currentPage,
                        perpage = pageSize,
                    )
                }

            resList.addAll(response.items)

            val responseCurrentPage = response.currentPage.toIntOrNull() ?: 0
            val responsePageCount = response.pages.toIntOrNull() ?: 0
            if (responseCurrentPage >= responsePageCount) {
                break
            } else {
                delay(500)
                currentPage++
            }
        }

        return resList
    }
}
