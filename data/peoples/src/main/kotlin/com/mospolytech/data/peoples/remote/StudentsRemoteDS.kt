package com.mospolytech.data.peoples.remote

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.peoples.model.db.StudentsDb
import com.mospolytech.data.peoples.model.entity.*
import com.mospolytech.domain.base.model.EducationType
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.base.utils.converters.LocalDateConverter.decode
import com.mospolytech.domain.peoples.model.EducationForm
import com.mospolytech.domain.peoples.model.Student
import org.jetbrains.exposed.sql.*
import kotlin.math.ceil

class StudentsRemoteDS {

    private suspend fun initTables() {
        MosPolyDb.transaction {
            SchemaUtils.create(StudentsDb)
        }
    }

    suspend fun getStudents() = MosPolyDb.transaction {
        StudentEntity.all().map { it.toModel() }
    }

    suspend fun getStudentsPaging(query: String, pageSize: Int, page: Int) =
        MosPolyDb.transaction {
            StudentEntity.all()
                .count { it.group != null && it.group!!.title.contains(query, ignoreCase = true) }
            val count = StudentEntity.find {
                (GroupsDb.title like query) or
                        (StudentsDb.lastName.lowerCase() like "%${query.lowercase()}%")
            }.count().toInt()
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
                page >= pagesCount -> null
                page <= 1 -> 2
                else -> page + 1
            }
            val list = StudentEntity.find {
                (GroupsDb.title like query) or
                        (StudentsDb.lastName.lowerCase() like "%${query.lowercase()}%")
            }
                .orderBy(StudentsDb.lastName to SortOrder.ASC)
                .limit(pageSize, offset)
                .mapLazy { it.toModel() }
                .sortedBy { it.firstName }
            PagingDTO(
                count = list.size,
                previousPage = previousPage,
                nextPage = nextPage,
                data = list
            )
        }


    suspend fun getStudents(group: String) =
        MosPolyDb.transaction {
            StudentEntity.find { (GroupsDb.title eq group) }
                .orderBy(StudentsDb.lastName to SortOrder.ASC)
                .mapLazy { it.toModel() }
                .sortedBy { it.firstName }
        }

    suspend fun clearData() {
        MosPolyDb.transaction {
            StudentsDb.deleteAll()
        }
    }

    suspend fun addStudent(student: Student) {
        MosPolyDb.transaction {
            val facultyEntity = student.faculty.let { faculty ->
                StudentFacultyEntity.new(faculty.id) {
                    title = faculty.title
                    titleShort = faculty.titleShort
                }
            }

            val directionEntity = student.direction.let { direction ->
                StudentDirectionEntity.new(direction.id) {
                    title = direction.title
                    code = direction.code
                }
            }

            val specializationEntity = student.specialization?.let { specialization ->
                StudentSpecializationEntity.new(specialization.id) {
                    title = specialization.title
                }
            }

            val groupEntity = student.group?.let { group ->
                GroupEntity.new(group.id) {
                    title = group.title
                    course = group.course
                    faculty = facultyEntity
                    direction = directionEntity
                }
            }

            StudentEntity.new(student.id) {
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
            }
        }
    }
}





