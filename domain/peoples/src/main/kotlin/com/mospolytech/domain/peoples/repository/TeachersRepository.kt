package com.mospolytech.domain.peoples.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Person
import com.mospolytech.domain.peoples.model.Teacher

interface TeachersRepository {
    suspend fun getTeacher(id: String): Teacher?

    suspend fun getTeachers(
        query: String = "",
        page: Int = 1,
        limit: Int = 100,
    ): PagingDTO<Person>

    suspend fun updateData(recreateDb: Boolean)
}
