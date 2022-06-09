package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.domain.peoples.model.Group
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class GroupEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, GroupEntity>(GroupsDb)

    var title by GroupsDb.title
    var course by GroupsDb.course
    var faculty by StudentFacultyEntity referencedOn  GroupsDb.faculty
    var direction by StudentDirectionEntity referencedOn  GroupsDb.direction


    fun toModel(): Group {
        return Group(
            id = id.value,
            title = title,
            course = course,
            faculty = faculty.toModel(),
            direction = direction.toModel()
        )
    }
}