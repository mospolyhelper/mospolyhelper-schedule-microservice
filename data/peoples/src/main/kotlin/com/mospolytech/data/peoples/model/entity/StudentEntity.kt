package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.StudentsDb
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.StudentShort
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, StudentEntity>(StudentsDb)

    var lkId by StudentsDb.lkId
    var name by StudentsDb.name
    var birthday by StudentsDb.birthday
    var avatar by StudentsDb.avatar
    var group by GroupEntity optionalReferencedOn StudentsDb.group
    var faculty by StudentsDb.faculty
    var direction by StudentsDb.direction
    var specialization by StudentsDb.specialization
    var educationType by StudentsDb.educationType
    var educationForm by StudentsDb.educationForm
    var payment by StudentsDb.payment
    var course by StudentsDb.course
    var years by StudentsDb.years
    var code by StudentsDb.code
    var dormitory by StudentsDb.dormitory
    var dormitoryRoom by StudentsDb.dormitoryRoom
    var branch by StudentsDb.branch
    var lastUpdate by StudentsDb.lastUpdate

    fun toModel(): Student {
        return Student(
            id = id.value,
            name = name,
            avatar = avatar,
            specialization = specialization,
            educationType = educationType,
            educationForm = educationForm,
            payment = payment,
            course = course,
            group = group?.toModel(),
            years = years,
            code = code,
            branch = branch,
            faculty = faculty,
            direction = direction,
        )
    }

    fun toModelShort(): StudentShort {
        return StudentShort(
            id = id.value,
            name = name,
            avatar = avatar,
            course = course,
            group = group?.toModel(),
        )
    }
}

val StudentEntity.description
    get() =
        buildString {
            group?.let {
                append(it.description)
            }
        }
