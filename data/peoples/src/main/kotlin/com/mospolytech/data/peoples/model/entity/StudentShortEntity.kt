package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.data.peoples.model.db.StudentsDb.nullable
import com.mospolytech.data.peoples.model.entity.StudentEntity.Companion.referrersOn
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.StudentShort
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentShortEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, StudentShortEntity>(StudentsDb)

    var firstName by StudentsDb.firstName
    var lastName by StudentsDb.lastName
    var middleName by StudentsDb.middleName

    var avatar by StudentsDb.avatar
    var group by GroupEntity optionalReferencedOn StudentsDb.group
    var course by StudentsDb.course




    private fun fullName(): String {
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

    fun toModel(): StudentShort {
        return StudentShort(
            id = id.value,
            name = fullName(),
            avatar = avatar,
            course = course,
            group = group?.toModel()
        )
    }
}