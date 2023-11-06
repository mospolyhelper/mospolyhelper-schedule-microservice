package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.peoples.model.GroupShort

interface GroupsRepository {
    suspend fun get(id: String): Group?

    suspend fun getAll(): List<Group>

    suspend fun getPagingShort(
        query: String,
        pageSize: Int,
        page: Int,
    ): PagingDTO<GroupShort>
}
