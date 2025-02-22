package com.mospolytech.data.peoples.repository

import com.mospolytech.data.base.retryIO
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.entity.TeacherEntity
import com.mospolytech.data.peoples.model.response.StaffResponse
import com.mospolytech.data.peoples.remote.TeachersRemoteDS
import com.mospolytech.data.peoples.service.TeachersService
import com.mospolytech.domain.auth.AuthRepository
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Person
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.peoples.repository.TeachersRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow

class TeachersRepositoryImpl(
    private val teachersService: TeachersService,
    private val teachersDS: TeachersRemoteDS,
    private val authRepository: AuthRepository,
) : TeachersRepository {
    override suspend fun getTeachers(
        query: String,
        page: Int,
        limit: Int,
    ): PagingDTO<Person> {
        return teachersDS.getTeachersPaging(query, limit, page)
    }

    override suspend fun getTeacher(id: String): Teacher? {
        return MosPolyDb.transaction {
            TeacherEntity.findById(id)?.toModel()
        }
    }

    override suspend fun updateData(recreateDb: Boolean) {
        if (recreateDb) {
            teachersDS.deleteTables()
            teachersDS.createTables()
        } else {
            teachersDS.ensureCreated()
            teachersDS.clearOldData()
        }

        val isSource1 = false

        if (isSource1) {
            downloadStudents1()
        } else {
            downloadStudents2()
        }
    }

    private suspend fun downloadStudents1() {
        val teachersFile = teachersService.downloadTeachers()
        // val teachersFile = File("""C:\Users\tipapro\Downloads\Telegram Desktop\response.xml""")
        try {
            val teachers = teachersService.parseTeachers(teachersFile)
            teachersDS.addTeachers(teachers)
        } finally {
            teachersFile.delete()
        }
    }

    private suspend fun downloadStudents2() {
        getAllTeachersLk()
            .buffer()
            .collect {
                teachersDS.addTeachers(it)
            }
    }

    private suspend fun getAllTeachersLk(): Flow<List<StaffResponse>> =
        flow {
            val token = authRepository.getTokenForMainUser()

            val pageSize = 1000
            var currentPage = 1

            while (true) {
                val response =
                    retryIO {
                        teachersService.getStaffLk(
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
