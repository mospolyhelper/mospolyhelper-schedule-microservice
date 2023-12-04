package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.domain.base.utils.ifNotEmpty
import com.mospolytech.domain.peoples.model.Person
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentShortEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, StudentShortEntity>(StudentsDb)

    var name by StudentsDb.name
    var avatar by StudentsDb.avatar
    var group by GroupEntity optionalReferencedOn StudentsDb.group
    var course by StudentsDb.course

    fun toModel(): Person {
        val group = group?.toModel()

        val description =
            buildString {
                group?.let {
                    append(it.title)
                }

                course?.let {
                    ifNotEmpty { append(", ") }
                    append("$course-й курс")
                }

                group?.let {
                    group.direction?.let {
                        ifNotEmpty { append(", ") }
                        append(group.direction)
                    }
                }
            }.ifEmpty { null }

        return Person(
            id = id.value,
            name = name,
            description = description,
            avatar = avatar,
        )
    }
}
