package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.peoples.model.Group

interface GroupsRepository {
    suspend fun get(id: String): Group?

    suspend fun getAll(): List<Group>
}
