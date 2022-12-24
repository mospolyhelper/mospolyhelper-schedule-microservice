package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.StudentShort
import com.mospolytech.domain.peoples.model.description
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentSafeEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, StudentSafeEntity>(StudentsDb)

    var firstName by StudentsDb.firstName
    var lastName by StudentsDb.lastName
    var middleName by StudentsDb.middleName

    var status by StudentsDb.status
    var sex by StudentsDb.sex
    var avatar by StudentsDb.avatar
    var faculty by StudentFacultyEntity optionalReferencedOn StudentsDb.faculty
    var direction by StudentDirectionEntity optionalReferencedOn StudentsDb.direction
    var group by GroupEntity optionalReferencedOn StudentsDb.group
    var specialization by StudentSpecializationEntity optionalReferencedOn StudentsDb.specialization
    var educationType by StudentsDb.educationType
    var educationForm by StudentsDb.educationForm
    var payment by StudentsDb.payment
    var course by StudentsDb.course
    var years by StudentsDb.years
    var code by StudentsDb.code
    var branch by StudentBranchEntity referencedOn StudentsDb.branch

    fun toModel(): Student {
        return Student(
            id = id.value,
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            sex = sex,
            avatar = avatar,
            birthday = null,
            specialization = specialization?.toModel(),
            educationType = educationType,
            educationForm = educationForm,
            payment = payment,
            course = course,
            group = group?.toModel(),
            years = years,
            code = code,
            dormitory = null,
            dormitoryRoom = null,
            branch = branch.toModel(),
            status = status,
            faculty = faculty?.toModel(),
            direction = direction?.toModel(),
        )
    }

    fun fullName(): String {
        return buildString {
            append(lastName)
            append(" ")
            append(firstName)
            middleName?.let {
                append(" ")
                append(middleName)
            }
        }
    }

    fun toModelShort(): StudentShort {
        return StudentShort(
            id = id.value,
            name = fullName(),
            avatar = avatar,
            course = course,
            group = group?.toModel(),
        )
    }
}

val StudentSafeEntity.description
    get() = buildString {
        group?.let {
            append(it.description)
        }
    }
