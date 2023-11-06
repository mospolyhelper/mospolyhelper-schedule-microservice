package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.domain.base.utils.ifNotEmpty
import com.mospolytech.domain.peoples.model.Teacher
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TeacherEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, TeacherEntity>(TeachersDb)

    var lkId by TeachersDb.lkId
    var name by TeachersDb.name
    var avatar by TeachersDb.avatar
    var stuffType by TeachersDb.stuffType
    var grade by TeachersDb.grade
    var departmentParent by TeachersDb.departmentParent
    var department by TeachersDb.department
    var email by TeachersDb.email
    var lastUpdate by TeachersDb.lastUpdate

    fun toModel(): Teacher {
        return Teacher(
            id = id.value,
            name = name,
            avatar = avatar,
            stuffType = stuffType,
            grade = grade,
            departmentParent = departmentParent,
            department = department,
            email = email,
        )
    }
}

val TeacherEntity.description: String
    get() {
        return buildString {
            grade?.let { append(it) }

            department?.let {
                ifNotEmpty { append(", ") }
                append(it)
            }

            departmentParent?.let {
                ifNotEmpty { append(", ") }
                append(it)
            }
        }
    }
