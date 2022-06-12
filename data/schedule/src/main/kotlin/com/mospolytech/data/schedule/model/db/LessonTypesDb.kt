package com.mospolytech.data.schedule.model.db

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable

object LessonTypesDb : UUIDTable() {
    val title = text("title").uniqueIndex()
    val shortTitle = text("short_title")
    val description = text("description")
    val isImportant = bool("is_important")
}