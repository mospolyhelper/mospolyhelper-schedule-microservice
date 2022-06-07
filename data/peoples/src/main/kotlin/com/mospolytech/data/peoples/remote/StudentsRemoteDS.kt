package com.mospolytech.data.peoples.remote

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.Students
import com.mospolytech.domain.base.model.EducationType
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.base.utils.converters.LocalDateConverter.decode
import com.mospolytech.domain.base.utils.converters.LocalDateConverter.encode
import com.mospolytech.domain.peoples.model.EducationForm
import com.mospolytech.domain.peoples.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.ceil

class StudentsRemoteDS {

    private suspend fun initTables() {
        MosPolyDb.transaction {
            SchemaUtils.create(Students)
        }
    }

    suspend fun getStudents() = withContext(Dispatchers.IO) {
        MosPolyDb.transaction {
            Students.selectAll().map { it.toStudent() }
        }
    }

    suspend fun getStudentsPaging(query: String, pageSize: Int, page: Int) = withContext(Dispatchers.IO) {
        MosPolyDb.transaction {
            val count = Students.select { (Students.group like query) or (Students.secondName.lowerCase() like "%${query.lowercase()}%") }.count().toInt()
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
            val list = Students.select { (Students.group like query) or (Students.secondName.lowerCase() like "%${query.lowercase()}%") }
                .orderBy(Students.secondName, SortOrder.ASC)
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
            Students.select { (Students.group eq group) }
                .orderBy(Students.secondName, SortOrder.ASC)
                .map { it.toStudent() }
                .sortedBy { it.secondName }
        }
    }

    suspend fun clearData() {
        MosPolyDb.transaction {
            Students.deleteAll()
        }
    }

    suspend fun addStudent(student: Student) {
        MosPolyDb.transaction {
            Students.insert {
                it[guid] = student.id
                it[firstName] = student.firstName
                it[secondName] = student.secondName
                it[surname] = student.surname
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
            id = this[Students.guid],
            firstName = this[Students.firstName],
            secondName = this[Students.secondName],
            surname = this[Students.surname],
            sex = this[Students.sex],
            avatar = this[Students.avatar],
            birthday = this[Students.birthday]?.decode(),
            faculty = this[Students.faculty],
            direction = this[Students.direction],
            specialization = this[Students.specialization],
            educationForm = this[Students.educationForm]?.let(EducationForm::valueOf),
            educationType = this[Students.educationType]?.let(EducationType::valueOf),
            payment = this[Students.payment],
            course = this[Students.course],
            group = this[Students.group],
            years = this[Students.years],
            dialogId =  this[Students.dialogId],
            additionalInfo =  this[Students.additionalInfo],
        )
    }
}





