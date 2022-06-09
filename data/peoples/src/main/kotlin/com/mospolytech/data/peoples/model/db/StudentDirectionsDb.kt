package com.mospolytech.data.peoples.model.db

import org.jetbrains.exposed.dao.id.IdTable

object StudentDirectionsDb : IdTable<String>() {
    override val id = TeachersDb.text("guid").entityId()
    val title = DepartmentsDb.text("title")
    val code = DepartmentsDb.text("code")
}