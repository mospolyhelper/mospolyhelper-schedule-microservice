package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.peoples.model.Group

interface GroupsRepository {
    suspend fun getOrPut(
        title: String,
        course: String,
        isEvening: Boolean,
    ): String
    suspend fun get(id: String): Group?

    suspend fun getAll(): List<Group>
}
