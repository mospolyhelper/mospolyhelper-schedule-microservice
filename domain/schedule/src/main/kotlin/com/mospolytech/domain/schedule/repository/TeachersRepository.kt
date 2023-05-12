package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.peoples.model.Teacher

interface TeachersRepository {
    suspend fun get(id: String): Teacher?
    suspend fun getAll(): List<Teacher>
}
