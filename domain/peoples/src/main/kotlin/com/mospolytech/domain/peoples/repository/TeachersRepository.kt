package com.mospolytech.domain.peoples.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Teacher

interface TeachersRepository {
    suspend fun getTeachers(
        name: String = "",
        page: Int = 1,
        limit: Int = 100,
    ): PagingDTO<Teacher>

    suspend fun getTeacher(name: String): Result<Teacher?>

    suspend fun getTeachers(): List<Teacher>

    suspend fun updateData(recreateDb: Boolean)
}
