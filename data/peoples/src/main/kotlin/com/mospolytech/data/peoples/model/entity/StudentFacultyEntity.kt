package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.StudentFacultiesDb
import com.mospolytech.domain.peoples.model.StudentFaculty
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentFacultyEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, StudentFacultyEntity>(StudentFacultiesDb)

    var title by StudentFacultiesDb.title
    var titleShort by StudentFacultiesDb.titleShort

    fun toModel(): StudentFaculty {
        return StudentFaculty(
            id = id.value,
            title = title,
            titleShort = titleShort
        )
    }
}
