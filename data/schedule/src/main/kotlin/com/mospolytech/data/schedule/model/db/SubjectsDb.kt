package com.mospolytech.data.schedule.model.db

import org.jetbrains.exposed.dao.id.IdTable

object SubjectsDb : IdTable<String>() {
    override val id = text("id").entityId()
    val title = text("title")
    val type = integer("type").nullable()

    override val primaryKey = PrimaryKey(id)
}