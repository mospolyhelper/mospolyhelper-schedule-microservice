package com.mospolytech.data.peoples.model.db

import org.jetbrains.exposed.dao.id.IdTable

object GroupsDb : IdTable<String>() {
    override val id = text("id").entityId()
    val title = text("title")
    val course = integer("course").nullable()
    val faculty = reference("faculty", StudentFacultiesDb).nullable()
    val direction = reference("direction", StudentDirectionsDb).nullable()

    override val primaryKey = PrimaryKey(id)
}
