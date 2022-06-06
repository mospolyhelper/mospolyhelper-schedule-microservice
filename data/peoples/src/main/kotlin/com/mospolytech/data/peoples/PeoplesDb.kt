package com.mospolytech.data.peoples

import com.mospolytech.domain.base.model.Department
import com.mospolytech.domain.base.model.EducationType
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.base.utils.converters.LocalDateConverter.decode
import com.mospolytech.domain.base.utils.converters.LocalDateConverter.encode
import com.mospolytech.domain.peoples.model.EducationForm
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.peoples.model.toForm
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PeoplesDb(config: ApplicationConfig) {

    companion object {
        private const val DB_URL = "jdbc:postgresql://devspare.mospolytech.ru:5432/postgres"
    }

    init {
        val login = config.propertyOrNull("ktor.pg_login")?.getString().orEmpty()
        val password = config.propertyOrNull("ktor.pg_password")?.getString().orEmpty()
        Database.connect(
            url = DB_URL,
            driver = "org.postgresql.Driver",
            user = login,
            password = password
        )
//        initTables()
    }

    private fun initTables() {
        transaction {
            SchemaUtils.create(Students)
            SchemaUtils.create(Teachers)
        }
    }

    suspend fun getStudents() = withContext(Dispatchers.IO) {
        transaction { Students.selectAll().map { it.toStudent() } }
    }

    suspend fun getTeachers() = withContext(Dispatchers.IO) {
        transaction { Teachers.selectAll().map { it.toTeacher() } }
    }

    suspend fun getTeachersPaging(query: String, pageSize: Int, page: Int) = withContext(Dispatchers.IO) {
        transaction {
            val count = Teachers.select { Teachers.name.lowerCase() like "%${query.lowercase()}%" }.count().toInt()
            val lastPageSize = if (count % pageSize != 0) count % pageSize else page
            val pagesCount = count / pageSize + 1
            val offset = when {
                count < 0 -> 0
                count < pageSize * page -> count - lastPageSize
                else -> (page - 1) * pageSize
            }.toLong()
            val previousPage = when {
                page <= 1 -> null
                page > pagesCount -> pagesCount - 1
                else -> page - 1
            }
            val nextPage = when {
                page <= 1 -> 2
                page >= pagesCount -> null
                else -> page + 1
            }
            val list = Teachers.select { Teachers.name.lowerCase() like "%${query.lowercase()}%" }
                .orderBy(Teachers.guid, SortOrder.DESC)
                .limit(pageSize, offset)
                .map { it.toTeacher() }
            PagingDTO(
                count = list.size,
                previousPage = previousPage,
                nextPage = nextPage,
                data = list
            )
        }
    }
    suspend fun getStudentsPaging(query: String, pageSize: Int, page: Int) = withContext(Dispatchers.IO) {
        transaction {
            val count = Students.select { (Students.group like query) or (Students.secondName.lowerCase() like "%${query.lowercase()}%") }.count().toInt()
            val lastPageSize = if (count % pageSize != 0) count % pageSize else page
            val pagesCount = count / pageSize + 1
            val offset = when {
                count < 0 -> 0
                count < pageSize * page -> count - lastPageSize
                else -> (page - 1) * pageSize
            }.toLong()
            val previousPage = when {
                page <= 1 -> null
                page > pagesCount -> pagesCount - 1
                else -> page - 1
            }
            val nextPage = when {
                page <= 1 -> 2
                page >= pagesCount -> null
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

    fun clearData() {
        transaction {
            Teachers.deleteAll()
            Students.deleteAll()
        }
    }

    fun addStudent(student: Student) {
        transaction {
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

    fun addTeacher(teacher: Teacher) {
        transaction {
            Teachers.insert {
                it[guid] = teacher.id
                it[name] = teacher.name
                it[avatar] = teacher.avatar
                it[stuffType] = teacher.stuffType
                it[grade] = teacher.grade
                it[departmentParentId] = teacher.departmentParent?.id
                it[departmentParent] = teacher.departmentParent?.title
                it[departmentId] = teacher.department?.id
                it[department] = teacher.department?.title
                it[email] = teacher.email
                it[sex] = teacher.sex
                it[birthday] = teacher.birthday?.encode()
                it[dialogId] = teacher.dialogId
                it[additionalInfo] = teacher.additionalInfo
            }
        }
    }


    private fun ResultRow.toTeacher(): Teacher {
        val departmentParent = this[Teachers.departmentParentId]?.let {
            Department(this[Teachers.departmentParentId]!!, this[Teachers.departmentParent]!!)
        }
        val department = this[Teachers.departmentId]?.let {
            Department(this[Teachers.departmentId]!!, this[Teachers.department]!!)
        }
        return Teacher(
            id = this[Teachers.guid],
            name = this[Teachers.name],
            avatar = this[Teachers.avatar],
            dialogId = this[Teachers.dialogId],
            stuffType = this[Teachers.stuffType],
            birthday = this[Teachers.birthday]?.decode(),
            grade = this[Teachers.grade],
            departmentParent = departmentParent,
            department = department,
            sex = this[Teachers.sex],
            email = this[Teachers.email],
            additionalInfo = this[Teachers.additionalInfo]
        )
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

    private object Students : IntIdTable() {
        val guid = text("guid")
        val firstName = text("firstName")
        val secondName = text("secondName")
        val surname = text("surname").nullable()
        val sex = text("sex")
        val avatar = text("avatar").nullable()
        val birthday = text("birthday").nullable()
        val faculty = text("faculty")
        val direction = text("direction")
        val specialization = text("specialization").nullable()
        val educationType = text("educationType").nullable()
        val educationForm = text("educationForm").nullable()
        val payment = bool("payment")
        val course = integer("course").nullable()
        val group = text("group").nullable()
        val years = text("years").nullable()
        val dialogId = text("dialogId").nullable()
        val additionalInfo = text("additionalInfo").nullable()
    }

    private object Teachers : IntIdTable() {
        val guid = text("guid")
        val name = text("name")
        val avatar = text("avatar").nullable()
        val stuffType = text("stuffType").nullable()
        val grade = text("grade").nullable()
        val departmentParentId = text("departmentParentId").nullable()
        val departmentParent = text("departmentParent").nullable()
        val departmentId = text("departmentId").nullable()
        val department = text("department").nullable()
        val email = text("email").nullable()
        val sex = text("sex").nullable()
        val birthday = text("birthday").nullable()
        val dialogId = text("dialogId").nullable()
        val additionalInfo = text("additionalInfo").nullable()
    }
}


