package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.DepartmentsDb
import com.mospolytech.domain.base.model.Department
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DepartmentEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, DepartmentEntity>(DepartmentsDb)

    var title by DepartmentsDb.title

    fun toModel(): Department {
        return Department(
            id = id.value,
            title = title
        )
    }
}
