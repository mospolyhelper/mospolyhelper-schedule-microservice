package com.mospolytech.data.peoples.remote

import com.mospolytech.data.base.createPagingDto
import com.mospolytech.data.base.upsert
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.peoples.model.db.StudentsDb
import com.mospolytech.data.peoples.model.entity.GroupEntity
import com.mospolytech.data.peoples.model.entity.StudentEntity
import com.mospolytech.data.peoples.model.entity.StudentShortEntity
import com.mospolytech.data.peoples.model.response.StudentsResponse
import com.mospolytech.data.peoples.model.xml.StudentEducationGroupXml
import com.mospolytech.data.peoples.model.xml.StudentInfoXml
import com.mospolytech.data.peoples.model.xml.StudentXml
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.utils.toGroupId
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.mapLazy
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.sequences.Sequence

class StudentsRemoteDS {
    suspend fun getStudents(group: String): List<Student> =
        MosPolyDb.transaction {
            val query =
                StudentsDb.leftJoin(GroupsDb)
                    .selectAll()
                    .where { (GroupsDb.title eq group) }
                    .orderBy(
                        StudentsDb.name to SortOrder.ASC,
                    )

            StudentEntity.wrapRows(query)
                .mapLazy { it.toModel() }
                .sortedBy { it.name }
        }

    suspend fun getShortStudents(
        query: String,
        pageSize: Int,
        page: Int,
    ) = MosPolyDb.transaction {
        createPagingDto(pageSize, page) { offset ->
            val dbQuery =
                StudentsDb.leftJoin(GroupsDb)
                    .select(StudentsDb.columns)
                    .orderBy(StudentsDb.name to SortOrder.ASC)

            if (query.isNotEmpty()) {
                dbQuery.andWhere {
                    (GroupsDb.title like "%$query%") or
                        (StudentsDb.name.lowerCase() like "%${query.lowercase()}%")
                }
            }

            StudentShortEntity.wrapRows(dbQuery)
                .limit(pageSize)
                .offset(offset.toLong())
                .mapLazy { it.toModel() }
                .toList()
        }
    }

    suspend fun addStudents(students: Sequence<StudentXml>) {
        students.forEach { student ->
            MosPolyDb.transaction {
                val birthday =
                    try {
                        LocalDate.parse(student.studentInfo.birthday, dateFormatter).toKotlinLocalDate()
                    } catch (e: Throwable) {
                        null
                    }
                val faculty = student.studentFaculty.title
                val direction = student.studentDir.title

                val groupEntity =
                    getGroupEntity(
                        group2 = student.studentEducationGroup,
                        faculty = faculty,
                        direction = direction,
                        course = student.studentEducationCourse.title.toIntOrNull(),
                    )

                val fullName = student.studentInfo.fullName()

                StudentEntity.upsert(
                    id = student.studentInfo.recordBookId,
                    findOp = {
                        (StudentsDb.name eq fullName) and
                            (StudentsDb.group eq groupEntity?.id) and
                            (StudentsDb.faculty eq faculty)
                    },
                    newId = { student.studentInfo.recordBookId },
                ) {
                    this.name = fullName
                    this.birthday = birthday
                    this.faculty = faculty
                    this.direction = direction
                    this.specialization = student.studentSpec.title.ifEmpty { null }
                    this.educationType = student.studentEducationForm.title
                    this.educationForm = student.studentForm.title
                    this.payment = student.studentPayment.title
                    this.course = student.studentEducationCourse.title.toIntOrNull()
                    this.group = groupEntity
                    this.years = student.studentEducationYear.title
                    this.code = student.studentCode.title
                    this.dormitory = student.dormitory.title.ifEmpty { null }
                    this.dormitoryRoom = student.dormitoryRoom.title.ifEmpty { null }
                    this.branch = student.studentBranch.title
                    this.lastUpdate = Clock.System.now()
                }
            }
        }
    }

    suspend fun addStudents(students: List<StudentsResponse>) {
        students.forEach { student ->
            MosPolyDb.transaction {
                val groupEntity = getGroupEntity(student.group, student.faculty)

                StudentEntity.upsert(
                    op = {
                        (StudentsDb.lkId eq student.id) or (
                            (StudentsDb.lkId eq null) and
                                (StudentsDb.name eq student.fio) and
                                (StudentsDb.group eq groupEntity?.id) and
                                (StudentsDb.faculty eq student.faculty)
                            )
                    },
                    newId = { UUID.randomUUID().toString() },
                ) {
                    this.lkId = student.id
                    this.name = student.fio
                    this.avatar = student.avatar.ifEmpty { null }
                    this.faculty = student.faculty
                    this.group = groupEntity
                    this.lastUpdate = Clock.System.now()
                }
            }
        }
    }

    private fun StudentInfoXml.fullName(): String {
        return buildString {
            append(lastName)
            append(" ")
            append(firstName)
            middleName.ifEmpty { null }?.let {
                append(" ")
                append(middleName)
            }
        }
    }

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private fun getGroupEntity(
        group2: StudentEducationGroupXml,
        faculty: String?,
        direction: String?,
        course: Int?,
    ): GroupEntity? {
        if (group2.title.isEmpty()) return null
        return GroupEntity.upsert(group2.guid) {
            this.title = group2.title
            this.course = course
            this.faculty = faculty
            this.direction = direction
        }
    }

    private fun getGroupEntity(
        title: String?,
        faculty: String?,
    ): GroupEntity? {
        if (title == null) return null
        return GroupEntity.upsert(title.toGroupId()) {
            this.title = title
            this.faculty = faculty
        }
    }

    suspend fun createTables() {
        MosPolyDb.transaction {
            SchemaUtils.create(
                StudentsDb,
                GroupsDb,
            )
        }
    }

    suspend fun deleteTables() {
        MosPolyDb.transaction {
            SchemaUtils.drop(
                StudentsDb,
                GroupsDb,
            )
        }
    }

    suspend fun ensureCreated() {
        MosPolyDb.transaction {
            SchemaUtils.createMissingTablesAndColumns(
                StudentsDb,
                GroupsDb,
            )
        }
    }

    suspend fun clearOldData() {
        MosPolyDb.transaction {
            val oneYearAgo = Clock.System.now().minus(DateTimePeriod(years = 1), TimeZone.UTC)
            StudentsDb.deleteWhere { StudentsDb.lastUpdate less oneYearAgo }
//            GroupsDb.deleteAll()
        }
    }
}
