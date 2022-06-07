package com.mospolytech.data.peoples.model.db

import org.jetbrains.exposed.dao.id.IdTable

object DepartmentsDb : IdTable<String>() {
    override val id = TeachersDb.text("guid").entityId()
    val title = text("name")
}