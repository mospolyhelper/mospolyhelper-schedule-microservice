package com.mospolytech.data.schedule.model.db

import org.jetbrains.exposed.dao.id.IntIdTable

object RecurrenceDb : IntIdTable() {
    val lesson = reference("lesson", LessonsDb).index()
    val recurrence = text("recurrence").index()
}
