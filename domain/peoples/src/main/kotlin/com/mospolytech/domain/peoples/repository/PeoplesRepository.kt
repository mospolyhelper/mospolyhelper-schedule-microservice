package com.mospolytech.domain.peoples.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher

interface PeoplesRepository {
    fun getTeachers(name: String = "", page: Int = 1, pageSize: Int = 100): PagingDTO<Teacher>
    suspend fun getStudents(name: String = "", page: Int = 1): Result<PagingDTO<Student>>
    suspend fun getClassmates(token: String): Result<List<Student>>
    fun getTeachers(): List<Teacher>
    suspend fun getStudents(): Result<List<String>>
}