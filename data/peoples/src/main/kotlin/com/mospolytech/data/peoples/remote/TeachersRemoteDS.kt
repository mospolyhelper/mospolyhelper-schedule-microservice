package com.mospolytech.data.peoples.remote

import com.mospolytech.data.base.createPagingDto
import com.mospolytech.data.base.findOrAllIfEmpty
import com.mospolytech.data.base.upsert
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.data.peoples.model.entity.TeacherEntity
import com.mospolytech.data.peoples.model.response.StaffResponse
import com.mospolytech.data.peoples.model.xml.EmployeeInfo
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import java.util.UUID
import kotlin.sequences.Sequence

class TeachersRemoteDS {
    suspend fun getTeacher(name: String) =
        MosPolyDb.transaction {
            TeacherEntity.find { TeachersDb.name eq name }
                .firstOrNull()
                ?.toModel()
        }

    suspend fun getTeachers() =
        MosPolyDb.transaction {
            TeacherEntity.all()
                .orderBy(TeachersDb.name to SortOrder.ASC)
                .map { it.toModel() }
        }

    suspend fun getTeachersPaging(
        query: String,
        pageSize: Int,
        page: Int,
    ) = MosPolyDb.transaction {
        createPagingDto(pageSize, page) { offset ->
            TeacherEntity.findOrAllIfEmpty(query) { TeachersDb.name.lowerCase() like "%${query.lowercase()}%" }
                .orderBy(TeachersDb.name to SortOrder.ASC)
                .limit(pageSize, offset.toLong())
                .map { it.toModel() }
        }
    }

    suspend fun addTeachers(teachers: Sequence<EmployeeInfo>) {
        // TODO поиск по фио и отделу
        teachers.forEach { teacher ->
            val stuffType =
                when (teacher.stuffType) {
                    "ППС" -> "Профессорско-преподавательский состав"
                    "АУП" -> "Административно-управленческий персонал"
                    "УВП" -> "Учебно-вспомогательный персонал"
                    "ПОП" -> "Прочий обслуживающий персонал"
                    "МОП" -> "Младший обслуживающий персонал"
                    "ИПР" -> "Инной педагогический работник"
                    "НТР" -> "Научно-технический работник"
                    "НР" -> "Научный работник"
                    else -> teacher.stuffType
                }

            MosPolyDb.transaction {
                // TODO Сделать поиск по другим полям, если сперва из лк загрузили
                TeacherEntity.upsert(teacher.guid) {
                    name = teacher.name
                    avatar = "https://e.mospolytech.ru/old/img/no_avatar.jpg"
                    this.stuffType = stuffType
                    grade = teacher.post
                    departmentParent = teacher.departmentParent
                    department = teacher.department
                    email = teacher.email
                    lastUpdate = Clock.System.now()
                }
            }
        }
    }

    suspend fun addTeachers(teachers: List<StaffResponse>) {
        // TODO поиск по фио и отделу
        teachers.forEach { teacher ->
            MosPolyDb.transaction {
                TeacherEntity.upsert(
                    op = {
                        TeachersDb.lkId eq teacher.id
                    },
                    newId = { UUID.randomUUID().toString() },
                ) {
                    this.name = teacher.fio
                    this.lkId = teacher.id
                    this.avatar = teacher.avatar
                    this.grade = teacher.post
                    this.department = teacher.division
                    this.email = teacher.email
                    this.lastUpdate = Clock.System.now()
                }
            }
        }
    }

    suspend fun createTables() {
        MosPolyDb.transaction {
            SchemaUtils.create(
                TeachersDb,
            )
        }
    }

    suspend fun ensureCreated() {
        MosPolyDb.transaction {
            SchemaUtils.createMissingTablesAndColumns(
                TeachersDb,
            )
        }
    }

    suspend fun clearData() {
        MosPolyDb.transaction {
            // TeachersDb.deleteAll()
        }
    }

    suspend fun deleteTables() {
        MosPolyDb.transaction {
            SchemaUtils.drop(
                TeachersDb,
            )
        }
    }
}
