package com.mospolytech.data.peoples

import com.mospolytech.domain.base.utils.converters.LocalDateConverter.encode
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        transaction {
            Students.selectAll().toList()
        }
    }

    suspend fun getTeachers() = withContext(Dispatchers.IO) {
        transaction {
            Teachers.selectAll().toList()
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
                it[educationForm] = student.educationType?.name
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


//    private fun ResultRow.toTeacher(): Teacher {
//        return Teacher(
//            id = this.fieldIndex,
//            name = name,
//            avatar = "https://e.mospolytech.ru/old/img/no_avatar.jpg",
//            dialogId = null,
//            stuffType = stuffType,
//            birthday = try {
//                LocalDate.parse(birthDate, dateFormatter)
//            } catch (e: Throwable) {
//                null
//            },
//            grade = post,
//            departmentParent = Department(
//                id = departmentParentGuid,
//                title = departmentParent
//            ),
//            department = if (department != null && departmentGuid != null) Department(
//                id = departmentGuid,
//                title = department
//            ) else null,
//            sex = Sex,
//            email = email,
//            additionalInfo = null
//        )
//    }
//
//    private fun ResultRow.toStudent(): Student {
//        return Student(
//            id = studentInfo.id,
//            firstName = studentInfo.firstName,
//            secondName = studentInfo.secondName,
//            surname = studentInfo.surname,
//            sex = studentInfo.sex,
//            avatar = "https://e.mospolytech.ru/old/img/no_avatar.jpg",
//            birthday = date,
//            faculty = studentFacult.name,
//            direction = studentDir.name,
//            specialization = studentSpec.name.ifEmpty { null },
//            educationForm = educationForm,
//            educationType = educationType,
//            payment = payment,
//            course = studentEducationCourse.name.toIntOrNull(),
//            group = studentEducationGroup.name.ifEmpty { null },
//            years = studentEducationYear.name,
//            dialogId = null,
//            additionalInfo = null,
//        )
//    }

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


