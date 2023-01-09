package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.domain.base.utils.ifNotEmpty
import com.mospolytech.domain.peoples.model.Teacher
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TeacherSafeEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, TeacherSafeEntity>(TeachersDb)

    var name by TeachersDb.name
    var avatar by TeachersDb.avatar
    var stuffType by TeachersDb.stuffType
    var grade by TeachersDb.grade
    var departmentParent by DepartmentEntity optionalReferencedOn TeachersDb.departmentParent
    var department by DepartmentEntity optionalReferencedOn TeachersDb.department
    var email by TeachersDb.email
    var sex by TeachersDb.sex
    var lastUpdate by TeachersDb.lastUpdate

    fun toModel(): Teacher {
        return Teacher(
            id = id.value,
            name = name,
            avatar = avatar,
            stuffType = stuffType,
            grade = grade,
            departmentParent = departmentParent?.toModel(),
            department = department?.toModel(),
            email = email,
            sex = sex,
            birthday = null,
        )
    }
}
val TeacherSafeEntity.description: String
    get() {
        return buildString {
            grade?.let { append(it) }

            department?.let {
                ifNotEmpty { append(", ") }
                append(it.title)
            }

            departmentParent?.let {
                ifNotEmpty { append(", ") }
                append(it.title)
            }
        }
    }
