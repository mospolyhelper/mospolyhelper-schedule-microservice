package com.mospolytech.data.schedule.model.db

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable

object LessonsDb : UUIDTable() {
    val type = reference("type", LessonTypesDb)
    val subject = reference("subject", SubjectsDb)
}