package com.mospolytech.data.schedule.model.db

import com.mospolytech.domain.schedule.model.lessonType.Importance
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object LessonsDb : UUIDTable() {
    val type = text("type")
    val subgroup = byte("subgroup").nullable()
    val subject = reference("subject", SubjectsDb)
    val startDateTime = datetime("start_datetime")
    val endDateTime = datetime("end_datetime")
    val importance = byte("importance").default(1)
}

fun Importance.toByte(): Byte {
    return when (this) {
        Importance.Low -> 0
        Importance.Normal -> 1
        Importance.High -> 2
    }
}
