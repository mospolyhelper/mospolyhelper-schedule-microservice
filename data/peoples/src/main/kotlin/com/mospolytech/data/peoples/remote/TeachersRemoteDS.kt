package com.mospolytech.data.peoples.remote

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

    private suspend fun initTables() {
       MosPolyDb.transaction {
            SchemaUtils.create(TeachersDb, DepartmentsDb)
        }
    }

    private suspend fun deleteTables() {
        MosPolyDb.transaction {
            SchemaUtils.drop(TeachersDb, DepartmentsDb)
        }
    }

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

    suspend fun getTeachersPaging(query: String, pageSize: Int, page: Int) = withContext(Dispatchers.IO) {
        MosPolyDb.transaction {
            val count = TeacherEntity.find { TeachersDb.name.lowerCase() like "%${query.lowercase()}%" }.count().toInt()
            val lastPageSize = if (count % pageSize != 0) count % pageSize else page
            val pagesCount = count / pageSize + ceil(count.toDouble() / pageSize).toInt()
            val offset = when {
                count < 0 -> 0
                count < pageSize * page -> count - lastPageSize
                else -> (page - 1) * pageSize
            }.toLong()
            val previousPage = when {
                page <= 1 -> null
                pagesCount <= 1 -> null
                page > pagesCount -> pagesCount - 1
                else -> page - 1
            }
            val nextPage = when {
                page <= 1 -> 2
                page >= pagesCount -> null
                else -> page + 1
            }
            val list = TeacherEntity.find { TeachersDb.name.lowerCase() like "%${query.lowercase()}%" }
                .limit(pageSize, offset)
                .mapLazy { it.toModel() }
                .sortedByDescending { it.name }

            PagingDTO(
                count = list.size,
                previousPage = previousPage,
                nextPage = nextPage,
                data = list
            )
        }
    }

    suspend fun addTeacher(teacher: Teacher) {
        MosPolyDb.transaction {
            val newDepartmentParent = teacher.departmentParent?.let {
                DepartmentEntity.new(it.id) {
                    title = it.title
                }
            }

            val newDepartment = teacher.department?.let {
                DepartmentEntity.new(it.id) {
                    title = it.title
                }
            }

            TeacherEntity.new(teacher.id) {
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

    suspend fun clearData() {
        MosPolyDb.transaction {
            TeachersDb.deleteAll()
        }
    }
}





