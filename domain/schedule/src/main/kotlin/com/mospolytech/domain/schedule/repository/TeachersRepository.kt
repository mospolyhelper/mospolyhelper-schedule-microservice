package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.peoples.model.Teacher

interface TeachersRepository {
    suspend fun findAndGetId(name: String): String
    fun get(id: String): Teacher?
}