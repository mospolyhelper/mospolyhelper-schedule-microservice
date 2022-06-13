package com.mospolytech.data.schedule.model.db

import com.mospolytech.data.peoples.model.db.GroupsDb
import org.jetbrains.exposed.sql.Table

object LessonToGroupsDb : Table() {
    val lesson = reference("lesson", LessonsDb)
    val group = reference("group", GroupsDb)

    override val primaryKey = PrimaryKey(lesson, group)
}