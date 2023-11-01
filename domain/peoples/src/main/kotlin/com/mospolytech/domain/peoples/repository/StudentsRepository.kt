package com.mospolytech.domain.peoples.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.StudentShort

interface StudentsRepository {
    suspend fun getStudents(query: String = "", page: Int = 1, pageSize: Int = 100): PagingDTO<Student>
    suspend fun getShortStudents(query: String = "", page: Int = 1, pageSize: Int = 100): PagingDTO<StudentShort>
    suspend fun getClassmates(token: String): Result<List<Student>>
    suspend fun getStudents(): List<Student>
    suspend fun getShortStudents(): List<StudentShort>
    suspend fun updateData(recreateDb: Boolean)
}
