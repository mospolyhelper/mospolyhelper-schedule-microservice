package com.mospolytech.data.schedule.model.db

import org.jetbrains.exposed.dao.id.UUIDTable

object SubjectsDb : UUIDTable() {
    val title = text("title").uniqueIndex()
    val type = text("type").nullable()
}
