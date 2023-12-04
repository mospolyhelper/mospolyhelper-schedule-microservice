package com.mospolytech.domain.peoples.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Person

interface StudentsRepository {
    suspend fun getShortStudents(
        query: String = "",
        page: Int = 1,
        limit: Int = 100,
    ): PagingDTO<Person>

    suspend fun getClassmates(token: String): Result<List<Person>>

    suspend fun updateData(recreateDb: Boolean)
}
