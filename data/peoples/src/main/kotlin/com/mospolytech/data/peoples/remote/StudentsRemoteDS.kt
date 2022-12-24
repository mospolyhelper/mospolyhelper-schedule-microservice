package com.mospolytech.data.peoples.remote

import com.mospolytech.data.base.upsert
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.data.peoples.model.entity.*
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Student
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import kotlin.sequences.Sequence

class StudentsRemoteDS {
    suspend fun getStudents() = MosPolyDb.transaction {
        StudentSafeEntity.all()
            .orderBy(
                StudentsDb.lastName to SortOrder.ASC,
                StudentsDb.firstName to SortOrder.ASC,
                StudentsDb.middleName to SortOrder.ASC,
            )
            .map { it.toModel() }
    }

    suspend fun getStudentsPaging(query: String, pageSize: Int, page: Int) =
        MosPolyDb.transaction {
            val offset = (page - 1) * pageSize
            val previousPage = if (page <= 1) null else page - 1
            val nextPage = if (page <= 1) 2 else page + 1

            val query = StudentsDb.leftJoin(GroupsDb)
                .slice(StudentsDb.columns)
                .select {
                    (GroupsDb.title like query) or
                        (StudentsDb.lastName.lowerCase() like "%${query.lowercase()}%")
                }.orderBy(
                    StudentsDb.lastName to SortOrder.ASC,
                    StudentsDb.firstName to SortOrder.ASC,
                    StudentsDb.middleName to SortOrder.ASC,
                )

            val list = StudentSafeEntity.wrapRows(query)
                .limit(pageSize, offset.toLong())
                .mapLazy { it.toModel() }
                .toList()
            PagingDTO(
                count = list.size,
                previousPage = previousPage,
                nextPage = nextPage,
                data = list,
            )
        }

    suspend fun getStudents(group: String) =
        MosPolyDb.transaction {
            val query = StudentsDb.leftJoin(GroupsDb)
                .select {
                    (GroupsDb.title eq group)
                }.orderBy(
                    StudentsDb.lastName to SortOrder.ASC,
                    StudentsDb.firstName to SortOrder.ASC,
                    StudentsDb.middleName to SortOrder.ASC,
                )

            StudentSafeEntity.wrapRows(query)
                .mapLazy { it.toModel() }
                .sortedBy { it.firstName }
        }

    suspend fun getShortStudents() =
        MosPolyDb.transaction {
            StudentShortEntity.find { StudentsDb.group neq null }
                .orderBy(
                    StudentsDb.lastName to SortOrder.ASC,
                    StudentsDb.firstName to SortOrder.ASC,
                    StudentsDb.middleName to SortOrder.ASC,
                )
                .map { it.toModel() }
        }

    suspend fun addStudent(student: Student) {
        MosPolyDb.transaction {
            val facultyEntity = getStudentFacultyEntity(student)
            val directionEntity = getStudentDirectionEntity(student)

            val groupEntity = getGroupEntity(student, facultyEntity, directionEntity)

            val specializationEntity = student.specialization?.let { specialization ->
                StudentSpecializationEntity.upsert(specialization.id) {
                    title = specialization.title
                }
            }

            val branchEntity = student.branch.let { branch ->
                StudentBranchEntity.upsert(branch.id) {
                    title = branch.title
                }
            }

            StudentEntity.upsert(student.id) {
                firstName = student.firstName
                lastName = student.lastName
                middleName = student.middleName
                sex = student.sex
                avatar = student.avatar
                birthday = student.birthday
                faculty = facultyEntity
                direction = directionEntity
                specialization = specializationEntity
                educationType = student.educationType
                educationForm = student.educationForm
                payment = student.payment
                course = student.course
                group = groupEntity
                years = student.years
                code = student.code
                dormitory = student.dormitory
                dormitoryRoom = student.dormitoryRoom
                branch = branchEntity
            }
        }
    }

    suspend fun addStudents(students: Sequence<Student>) {
        MosPolyDb.transaction {
            students.forEach { student ->
                val facultyEntity = getStudentFacultyEntity(student)
                val directionEntity = getStudentDirectionEntity(student)

                val groupEntity = getGroupEntity(student, facultyEntity, directionEntity)

                val specializationEntity = student.specialization?.let { specialization ->
                    StudentSpecializationEntity.upsert(specialization.id) {
                        title = specialization.title
                    }
                }

                val branchEntity = student.branch.let { branch ->
                    StudentBranchEntity.upsert(branch.id) {
                        title = branch.title
                    }
                }

                StudentEntity.upsert(student.id) {
                    firstName = student.firstName
                    lastName = student.lastName
                    middleName = student.middleName

                    status = student.status

                    sex = student.sex
                    avatar = student.avatar
                    birthday = student.birthday
                    faculty = facultyEntity
                    direction = directionEntity
                    specialization = specializationEntity
                    educationType = student.educationType
                    educationForm = student.educationForm
                    payment = student.payment
                    course = student.course
                    group = groupEntity
                    years = student.years
                    code = student.code
                    dormitory = student.dormitory
                    dormitoryRoom = student.dormitoryRoom
                    branch = branchEntity
                    lastUpdate = Clock.System.now()
                }
            }
        }
    }

    private fun getStudentFacultyEntity(student: Student): StudentFacultyEntity? {
        return student.faculty?.let {
            StudentFacultyEntity.upsert(it.id) {
                title = it.title
                titleShort = it.titleShort
            }
        }
    }

    private fun getStudentDirectionEntity(student: Student): StudentDirectionEntity? {
        return student.direction?.let {
            StudentDirectionEntity.upsert(it.id) {
                title = it.title
                code = it.code
            }
        }
    }

    private fun getGroupEntity(
        student: Student,
        facultyEntity: StudentFacultyEntity?,
        directionEntity: StudentDirectionEntity?,
    ): GroupEntity? {
        return student.group?.let { group ->
            GroupEntity.upsert(group.id) {
                title = group.title
                course = group.course
                faculty = facultyEntity
                direction = directionEntity
            }
        }
    }

    suspend fun createTables() {
        MosPolyDb.transaction {
            SchemaUtils.create(
                StudentsDb,
                StudentBranchesDb,
                StudentDirectionsDb,
                StudentFacultiesDb,
                StudentSpecializationsDb,
                GroupsDb,
            )
        }
    }

    suspend fun deleteTables() {
        MosPolyDb.transaction {
            SchemaUtils.drop(
                StudentsDb,
                StudentBranchesDb,
                StudentDirectionsDb,
                StudentFacultiesDb,
                StudentSpecializationsDb,
                GroupsDb,
            )
        }
    }

    suspend fun clearData() {
        MosPolyDb.transaction {
            StudentsDb.deleteAll()
            StudentBranchesDb.deleteAll()
            StudentDirectionsDb.deleteAll()
            StudentFacultiesDb.deleteAll()
            StudentSpecializationsDb.deleteAll()
            GroupsDb.deleteAll()
        }
    }
}
