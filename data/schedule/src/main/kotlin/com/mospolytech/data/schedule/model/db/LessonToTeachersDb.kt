package com.mospolytech.data.schedule.model.db

import com.mospolytech.data.peoples.model.db.TeachersDb
import org.jetbrains.exposed.sql.Table

object LessonToTeachersDb : Table() {
    val lesson = reference("lesson", LessonsDb)
    val teacher = reference("teacher", TeachersDb)

    override val primaryKey = PrimaryKey(lesson, teacher)
}