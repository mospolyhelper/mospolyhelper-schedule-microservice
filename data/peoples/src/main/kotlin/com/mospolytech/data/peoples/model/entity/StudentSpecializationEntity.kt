package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.StudentSpecializationsDb
import com.mospolytech.domain.peoples.model.StudentSpecialization
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentSpecializationEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, StudentSpecializationEntity>(StudentSpecializationsDb)

    var title by StudentSpecializationsDb.title

    fun toModel(): StudentSpecialization {
        return StudentSpecialization(
            id = id.value,
            title = title
        )
    }
}