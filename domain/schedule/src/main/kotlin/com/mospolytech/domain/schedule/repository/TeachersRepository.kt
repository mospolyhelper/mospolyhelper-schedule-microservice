package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Teacher

interface TeachersRepository {
    suspend fun get(id: String): Teacher?

    suspend fun getPaging(
        query: String,
        pageSize: Int,
        page: Int,
    ): PagingDTO<Teacher>
}
