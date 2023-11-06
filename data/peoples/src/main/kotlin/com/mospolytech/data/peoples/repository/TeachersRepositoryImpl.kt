package com.mospolytech.data.peoples.repository

import com.mospolytech.data.base.retryIO
import com.mospolytech.data.peoples.model.response.StaffResponse
import com.mospolytech.data.peoples.remote.TeachersRemoteDS
import com.mospolytech.data.peoples.service.TeachersService
import com.mospolytech.domain.auth.AuthRepository
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.peoples.repository.TeachersRepository
import kotlinx.coroutines.delay
import java.io.File

class TeachersRepositoryImpl(
    private val teachersService: TeachersService,
    private val teachersDS: TeachersRemoteDS,
    private val authRepository: AuthRepository,
) : TeachersRepository {
    override suspend fun getTeachers(
        name: String,
        page: Int,
        pageSize: Int,
    ) = teachersDS.getTeachersPaging(name, pageSize, page)

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
            teachersDS.ensureCreated()
            teachersDS.clearData()
        }

        // val teachersFile = teachersService.downloadTeachers()
        val teachersFile = File("""C:\Users\tipapro\Downloads\Telegram Desktop\response.xml""")
        try {
            val teachers = teachersService.parseTeachers(teachersFile)
            teachersDS.addTeachers(teachers)
        } finally {
            teachersFile.delete()
        }
    }

    suspend fun updateData2(recreateDb: Boolean) {
        if (recreateDb) {
            teachersDS.deleteTables()
            teachersDS.createTables()
        } else {
            teachersDS.ensureCreated()
            teachersDS.clearData()
        }

        val teachers = getAllTeachersLk()
        teachersDS.addTeachers(teachers)
    }

    private suspend fun getAllTeachersLk(): List<StaffResponse> {
        val token = authRepository.getTokenForMainUser()

        val resList = mutableListOf<StaffResponse>()
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
