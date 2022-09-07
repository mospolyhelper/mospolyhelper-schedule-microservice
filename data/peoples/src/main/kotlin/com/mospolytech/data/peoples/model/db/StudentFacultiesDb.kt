package com.mospolytech.data.peoples.model.db

import org.jetbrains.exposed.dao.id.IdTable

object StudentFacultiesDb : IdTable<String>() {
    override val id = text("id").entityId()
    val title = text("title")
    val titleShort = text("title_short").nullable()

    override val primaryKey = PrimaryKey(id)
}
