package com.mospolytech.data.peoples.remote

import com.mospolytech.data.base.upsert
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.DepartmentsDb
import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.data.peoples.model.entity.DepartmentEntity
import com.mospolytech.data.peoples.model.entity.TeacherEntity
import com.mospolytech.data.peoples.model.entity.TeacherSafeEntity
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Teacher
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import kotlin.sequences.Sequence

class TeachersRemoteDS {
    suspend fun getTeacher(name: String) = MosPolyDb.transaction {
        TeacherSafeEntity.find { TeachersDb.name eq name }
            .firstOrNull()
            ?.toModel()
    }

    suspend fun getTeachers() = MosPolyDb.transaction {
        TeacherSafeEntity.all()
            .orderBy(TeachersDb.name to SortOrder.ASC)
            .map { it.toModel() }
    }

    suspend fun getTeachersPaging(query: String, pageSize: Int, page: Int) =
        MosPolyDb.transaction {
            val offset = (page - 1) * pageSize
            val previousPage = if (page <= 1) null else page - 1
            val nextPage = if (page <= 1) 2 else page + 1

            val list = TeacherSafeEntity.find { TeachersDb.name.lowerCase() like "%${query.lowercase()}%" }
                .orderBy(TeachersDb.name to SortOrder.ASC)
                .limit(pageSize, offset.toLong())
                .map { it.toModel() }

            PagingDTO(
                count = list.size,
                previousPage = previousPage,
                nextPage = nextPage,
                data = list,
            )
        }

    suspend fun addTeachers(teachers: Sequence<Teacher>) {
        teachers.forEach { teacher ->
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
                    lastUpdate = Clock.System.now()
                }
            }
        }
    }

    suspend fun createTables() {
        MosPolyDb.transaction {
            SchemaUtils.create(
                TeachersDb,
                DepartmentsDb,
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
                DepartmentsDb,
            )
        }
    }
}
