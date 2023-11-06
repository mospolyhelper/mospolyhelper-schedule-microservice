package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.domain.peoples.model.StudentShort
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentShortEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, StudentShortEntity>(StudentsDb)

    var name by StudentsDb.name
    var avatar by StudentsDb.avatar
    var group by GroupEntity optionalReferencedOn StudentsDb.group
    var course by StudentsDb.course

    fun toModel(): StudentShort {
        return StudentShort(
            id = id.value,
            name = name,
            avatar = avatar,
            course = course,
            group = group?.toModel(),
        )
    }
}
