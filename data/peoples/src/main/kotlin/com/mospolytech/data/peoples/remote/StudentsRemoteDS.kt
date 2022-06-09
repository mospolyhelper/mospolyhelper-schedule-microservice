package com.mospolytech.data.peoples.remote

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.StudentsDb
import com.mospolytech.domain.base.model.EducationType
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.base.utils.converters.LocalDateConverter.decode
import com.mospolytech.domain.base.utils.converters.LocalDateConverter.encode
import com.mospolytech.domain.peoples.model.EducationForm
import com.mospolytech.domain.peoples.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import kotlin.math.ceil

class StudentsRemoteDS {

    private suspend fun initTables() {
        MosPolyDb.transaction {
            SchemaUtils.create(StudentsDb)
        }
    }

    suspend fun getStudents() = withContext(Dispatchers.IO) {
        MosPolyDb.transaction {
            StudentsDb.selectAll().map { it.toStudent() }
        }
    }

    suspend fun getStudentsPaging(query: String, pageSize: Int, page: Int) = withContext(Dispatchers.IO) {
        MosPolyDb.transaction {
            val count = StudentsDb.select { (StudentsDb.group like query) or (StudentsDb.lastName.lowerCase() like "%${query.lowercase()}%") }.count().toInt()
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
            val list = StudentsDb.select { (StudentsDb.group like query) or (StudentsDb.lastName.lowerCase() like "%${query.lowercase()}%") }
                .orderBy(StudentsDb.lastName, SortOrder.ASC)
                .limit(pageSize, offset)
                .map { it.toStudent() }
                .sortedBy { it.secondName }
            PagingDTO(
                count = list.size,
                previousPage = previousPage,
                nextPage = nextPage,
                data = list
            )
        }
    }


    suspend fun getStudents(group: String) = withContext(Dispatchers.IO) {
        MosPolyDb.transaction {
            StudentsDb.select { (StudentsDb.group eq group) }
                .orderBy(StudentsDb.lastName, SortOrder.ASC)
                .map { it.toStudent() }
                .sortedBy { it.secondName }
        }
    }

    suspend fun clearData() {
        MosPolyDb.transaction {
            StudentsDb.deleteAll()
        }
    }

    suspend fun addStudent(student: Student) {
        MosPolyDb.transaction {
            StudentsDb.insert {
                it[guid] = student.id
                it[firstName] = student.firstName
                it[lastName] = student.secondName
                it[middleName] = student.surname
                it[sex] = student.sex
                it[avatar] = student.avatar
                it[birthday] = student.birthday?.encode()
                it[faculty] = student.faculty
                it[direction] = student.direction
                it[specialization] = student.specialization
                it[educationType] = student.educationType?.name
                it[educationForm] = student.educationForm?.name
                it[payment] = student.payment
                it[course] = student.course
                it[group] = student.group
                it[years] = student.years
                it[dialogId] = student.dialogId
                it[additionalInfo] = student.additionalInfo
            }
        }
    }

    private fun ResultRow.toStudent(): Student {
        return Student(
            id = this[StudentsDb.guid],
            firstName = this[StudentsDb.firstName],
            secondName = this[StudentsDb.lastName],
            surname = this[StudentsDb.middleName],
            sex = this[StudentsDb.sex],
            avatar = this[StudentsDb.avatar],
            birthday = this[StudentsDb.birthday]?.decode(),
            faculty = this[StudentsDb.faculty],
            direction = this[StudentsDb.direction],
            specialization = this[StudentsDb.specialization],
            educationForm = this[StudentsDb.educationForm]?.let(EducationForm::valueOf),
            educationType = this[StudentsDb.educationType]?.let(EducationType::valueOf),
            payment = this[StudentsDb.payment],
            course = this[StudentsDb.course],
            group = this[StudentsDb.group],
            years = this[StudentsDb.years],
            dialogId =  this[StudentsDb.dialogId],
            additionalInfo =  this[StudentsDb.additionalInfo],
        )
    }
}





