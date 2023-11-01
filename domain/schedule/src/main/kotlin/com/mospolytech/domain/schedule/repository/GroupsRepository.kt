package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Group

interface GroupsRepository {
    suspend fun get(id: String): Group?

    suspend fun getAll(): List<Group>

    suspend fun getPaging(query: String, pageSize: Int, page: Int): PagingDTO<Group>
}
