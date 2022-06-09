package com.mospolytech.data.peoples.model.db

import org.jetbrains.exposed.dao.id.IdTable

object GroupsDb : IdTable<String>() {
    override val id = TeachersDb.text("guid").entityId()
    val title = DepartmentsDb.text("title")
    val course = text("course")
    val faculty = reference("faculty", StudentFacultiesDb)
    val direction = reference("direction", StudentDirectionsDb)
}