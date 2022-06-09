package com.mospolytech.domain.peoples.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher

interface StudentsRepository {
    suspend fun getStudents(name: String = "", page: Int = 1, pageSize: Int = 100): PagingDTO<Student>
    suspend fun getClassmates(token: String): Result<List<Student>>
    suspend fun getStudents(): List<Student>
    suspend fun updateData()
}