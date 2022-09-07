package com.mospolytech.data.schedule.model.db

import org.jetbrains.exposed.sql.Table

object LessonToPlacesDb : Table() {
    val lesson = reference("lesson", LessonsDb)
    val place = reference("place", PlacesDb)

    override val primaryKey = PrimaryKey(lesson, place)
}
