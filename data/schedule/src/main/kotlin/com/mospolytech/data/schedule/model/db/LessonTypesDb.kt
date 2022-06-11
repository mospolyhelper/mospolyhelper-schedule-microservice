package com.mospolytech.data.schedule.model.db

import org.jetbrains.exposed.dao.id.IdTable

object LessonTypesDb : IdTable<String>() {
    override val id = text("id").entityId()
    val title = text("title").uniqueIndex()
    val shortTitle = text("short_title")
    val description = text("description")
    val isImportant = bool("is_important")

    override val primaryKey = PrimaryKey(id)
}