package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.domain.peoples.model.Teacher
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TeacherEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, TeacherEntity>(TeachersDb)

    var name by TeachersDb.name
    var avatar by TeachersDb.avatar
    var stuffType by TeachersDb.stuffType
    var grade by TeachersDb.grade
    var departmentParent by DepartmentEntity optionalReferencedOn TeachersDb.departmentParent
    var department by DepartmentEntity optionalReferencedOn TeachersDb.department
    var email by TeachersDb.email
    var sex by TeachersDb.sex
    var birthday by TeachersDb.birthday
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
            birthday = birthday,
        )
    }
}
