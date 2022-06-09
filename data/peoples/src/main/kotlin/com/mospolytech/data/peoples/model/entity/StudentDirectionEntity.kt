package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.StudentDirectionsDb
import com.mospolytech.domain.peoples.model.StudentDirection
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentDirectionEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, StudentDirectionEntity>(StudentDirectionsDb)

    var title by StudentDirectionsDb.title
    var code by StudentDirectionsDb.code

    fun toModel(): StudentDirection {
        return StudentDirection(
            id = id.value,
            title = title,
            code = code
        )
    }
}