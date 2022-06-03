package com.mospolytech.domain.peoples.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher

interface PeoplesRepository {
    fun getTeachers(name: String = "", page: Int = 1, pageSize: Int = 100): PagingDTO<Teacher>
    fun getStudents(name: String = "", page: Int = 1, pageSize: Int = 100): PagingDTO<Student>
    fun getClassmates(group: String): List<Student>
    fun getTeachers(): List<Teacher>
    fun getStudents(): List<Student>
}