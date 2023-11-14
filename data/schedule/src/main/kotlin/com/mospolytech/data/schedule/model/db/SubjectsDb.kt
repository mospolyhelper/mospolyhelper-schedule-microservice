package com.mospolytech.data.schedule.model.db

import org.jetbrains.exposed.dao.id.IdTable

object SubjectsDb : IdTable<String>() {
    override val id = text("id").entityId()
    val title = text("title").uniqueIndex()
    val type = text("type").nullable()
    val description = text("description").nullable()

    override val primaryKey = PrimaryKey(id)
}
