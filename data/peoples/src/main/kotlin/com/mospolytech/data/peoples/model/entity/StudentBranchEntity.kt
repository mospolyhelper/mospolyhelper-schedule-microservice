package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.StudentBranchesDb
import com.mospolytech.domain.peoples.model.StudentBranch
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentBranchEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, StudentBranchEntity>(StudentBranchesDb)

    var title by StudentBranchesDb.title

    fun toModel(): StudentBranch {
        return StudentBranch(
            id = id.value,
            title = title,
        )
    }
}
