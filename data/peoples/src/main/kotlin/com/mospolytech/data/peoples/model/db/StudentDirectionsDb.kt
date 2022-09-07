package com.mospolytech.data.peoples.model.db

import org.jetbrains.exposed.dao.id.IdTable

object StudentDirectionsDb : IdTable<String>() {
    override val id = text("id").entityId()
    val title = text("title")
    val code = text("code")

    override val primaryKey = PrimaryKey(id)
}
