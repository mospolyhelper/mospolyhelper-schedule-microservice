package com.mospolytech.data.peoples.remote

import com.mospolytech.data.base.createPagingDto
import com.mospolytech.data.base.findOrAllIfEmpty
import com.mospolytech.data.base.upsert
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.data.peoples.model.entity.TeacherEntity
import com.mospolytech.data.peoples.model.response.StaffResponse
import com.mospolytech.data.peoples.model.xml.EmployeeInfo
import com.mospolytech.domain.peoples.model.toPerson
import kotlinx.datetime.Clock
import kotlinx.datetime.minus
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
                .map { it.toModel().toPerson() }
        }
    }

    suspend fun addTeachers(teachers: Sequence<EmployeeInfo>) {
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
                TeacherEntity.upsert(
                    id = teacher.guid,
                    findOp = {
                        (TeachersDb.name eq teacher.name) and
                            (TeachersDb.grade eq teacher.post) and
                            (TeachersDb.department eq teacher.department)
                    },
                    newId = {
                        teacher.guid
                    },
                ) {
                    this.name = teacher.name
                    this.stuffType = stuffType
                    this.grade = teacher.post
                    this.departmentParent = teacher.departmentParent
                    this.department = teacher.department
                    this.email = teacher.email
                    this.lastUpdate = Clock.System.now()
                }
            }
        }
    }

    suspend fun addTeachers(teachers: List<StaffResponse>) {
        teachers.forEach { teacher ->
            MosPolyDb.transaction {
                TeacherEntity.upsert(
                    op = {
                        (TeachersDb.lkId eq teacher.id) or (
                            (TeachersDb.lkId eq null) and
                                (TeachersDb.name eq teacher.fio) and
                                (TeachersDb.grade eq teacher.post) and
                                (TeachersDb.department eq teacher.division)
                            )
                    },
                    newId = { UUID.randomUUID().toString() },
                ) {
                    this.name = teacher.fio
                    this.lkId = teacher.id
                    this.avatar = teacher.avatar.ifEmpty { null }
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

    suspend fun clearOldData() {
//        MosPolyDb.transaction {
//            val oneYearAgo = Clock.System.now().minus(DateTimePeriod(years = 1), TimeZone.UTC)
//            TeachersDb.deleteWhere {
//                (TeachersDb.lastUpdate less oneYearAgo) and
//                    notExists(
//                        LessonToTeachersDb.select { LessonToTeachersDb.teacher eq TeachersDb.id }
//                    )
//            }
//        }
    }

    suspend fun deleteTables() {
        MosPolyDb.transaction {
            SchemaUtils.drop(
                TeachersDb,
            )
        }
    }
}
