package com.mospolytech.data.schedule.model.db

import org.jetbrains.exposed.sql.Table

object LessonToLessonDateTimesDb : Table() {
    val lesson = reference("lesson", LessonsDb)
    val time = reference("time", LessonDateTimesDb)

    override val primaryKey = PrimaryKey(lesson, time)
}