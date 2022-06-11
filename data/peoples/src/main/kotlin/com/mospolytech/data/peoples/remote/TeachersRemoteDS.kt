package com.mospolytech.data.peoples.remote

import com.mospolytech.data.base.upsert
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.DepartmentsDb
import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.data.peoples.model.entity.DepartmentEntity
import com.mospolytech.data.peoples.model.entity.TeacherEntity
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Teacher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import kotlin.math.ceil

class TeachersRemoteDS {
    suspend fun getTeacher(name: String) = withContext(Dispatchers.IO) {
        MosPolyDb.transaction {
            TeacherEntity.find { TeachersDb.name eq name }
                .firstOrNull()
                ?.toModel()

        }
    }

    suspend fun getTeachers() = withContext(Dispatchers.IO) {
        MosPolyDb.transaction {
            TeacherEntity.all().map { it.toModel() }
        }
    }

    suspend fun getTeachersPaging(query: String, pageSize: Int, page: Int) =
        MosPolyDb.transaction {
            val offset = (page - 1) * pageSize
            val previousPage = if (page <= 1) null else page - 1
            val nextPage = if (page <= 1) 2 else page + 1

            val list = TeacherEntity.find { TeachersDb.name.lowerCase() like "%${query.lowercase()}%" }
                .limit(pageSize, offset.toLong())
                .mapLazy { it.toModel() }
                .sortedByDescending { it.name }

            PagingDTO(
                count = list.size,
                previousPage = previousPage,
                nextPage = nextPage,
                data = list
            )
        }

    suspend fun addTeacher(teacher: Teacher) {
        MosPolyDb.transaction {
            val newDepartmentParent = teacher.departmentParent?.let {
                DepartmentEntity.upsert(it.id) {
                    title = it.title
                }
            }

            val newDepartment = teacher.department?.let {
                DepartmentEntity.upsert(it.id) {
                    title = it.title
                }
            }

            TeacherEntity.upsert(teacher.id) {
                name = teacher.name
                avatar = teacher.avatar
                stuffType = teacher.stuffType
                grade = teacher.grade
                departmentParent = newDepartmentParent
                department = newDepartment
                email = teacher.email
                sex = teacher.sex
                birthday = teacher.birthday
            }
        }
    }

    suspend fun createTables() {
        MosPolyDb.transaction {
            SchemaUtils.create(
                TeachersDb,
                DepartmentsDb
            )
        }
    }

    suspend fun clearData() {
        MosPolyDb.transaction {
            TeachersDb.deleteAll()
            DepartmentsDb.deleteAll()
        }
    }

    suspend fun deleteTables() {
        MosPolyDb.transaction {
            SchemaUtils.drop(
                TeachersDb,
                DepartmentsDb
            )
        }
    }
}





