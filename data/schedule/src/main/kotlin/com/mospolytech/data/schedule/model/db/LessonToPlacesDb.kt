package com.mospolytech.data.schedule.model.db

import com.mospolytech.data.peoples.model.db.GroupsDb
import org.jetbrains.exposed.sql.Table

object LessonToPlacesDb : Table() {
    val lesson = reference("lesson", LessonsDb)
    val place = reference("place", GroupsDb)

    override val primaryKey = PrimaryKey(lesson, place)
}