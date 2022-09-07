package com.mospolytech.data.peoples.model.db

import org.jetbrains.exposed.dao.id.IdTable

object DepartmentsDb : IdTable<String>() {
    override val id = text("id").entityId()
    val title = text("title")

    override val primaryKey = PrimaryKey(id)
}
