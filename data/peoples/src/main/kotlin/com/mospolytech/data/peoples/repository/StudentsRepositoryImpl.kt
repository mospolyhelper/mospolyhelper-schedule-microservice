package com.mospolytech.data.peoples.repository

import com.mospolytech.data.base.retryIO
import com.mospolytech.data.peoples.model.response.StudentsResponse
import com.mospolytech.data.peoples.remote.StudentsRemoteDS
import com.mospolytech.data.peoples.service.StudentsService
import com.mospolytech.domain.auth.AuthRepository
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Person
import com.mospolytech.domain.peoples.model.toPerson
import com.mospolytech.domain.peoples.repository.StudentsRepository
import com.mospolytech.domain.services.personal.PersonalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow

class StudentsRepositoryImpl(
    private val studentsService: StudentsService,
    private val studentsDS: StudentsRemoteDS,
    private val personalRepository: PersonalRepository,
    private val authRepository: AuthRepository,
) : StudentsRepository {
    override suspend fun getShortStudents(
        query: String,
        page: Int,
        limit: Int,
    ): PagingDTO<Person> {
        return studentsDS.getShortStudents(query, limit, page)
    }

    override suspend fun getClassmates(token: String): Result<List<Person>> {
        return personalRepository.getPersonalGroup(token).mapCatching { group ->
            if (group == null) {
                emptyList()
            } else {
                studentsDS.getStudents(group).map { it.toPerson() }
            }
        }
    }

    override suspend fun updateData(recreateDb: Boolean) {
        if (recreateDb) {
            studentsDS.deleteTables()
            studentsDS.createTables()
        } else {
            studentsDS.ensureCreated()
            studentsDS.clearOldData()
        }

        val isSource1 = false

        if (isSource1) {
            downloadStudents1()
        } else {
            downloadStudents2()
        }
    }

    private suspend fun downloadStudents1() {
        val studentsFile = studentsService.downloadStudents()
        try {
            val students = studentsService.parseStudents(studentsFile)
            studentsDS.addStudents(students)
        } finally {
            studentsFile.delete()
        }
    }

    private suspend fun downloadStudents2() {
        getAllStudentsLk()
            .buffer()
            .collect {
                studentsDS.addStudents(it)
            }
    }

    private fun getAllStudentsLk(): Flow<List<StudentsResponse>> =
        flow {
            val token = authRepository.getTokenForMainUser()

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

                emit(response.items)

                val responseCurrentPage = response.currentPage.toIntOrNull() ?: 0
                val responsePageCount = response.pages.toIntOrNull() ?: 0
                if (responseCurrentPage >= responsePageCount) {
                    break
                } else {
                    delay(500)
                    currentPage++
                }
            }
        }
}
