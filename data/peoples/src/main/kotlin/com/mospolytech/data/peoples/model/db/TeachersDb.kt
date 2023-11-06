package com.mospolytech.data.peoples.model.db

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object TeachersDb : IdTable<String>() {
    override val id: Column<EntityID<String>> = text("id").entityId()
    val lkId = text("lk_id").nullable().index()
    val name = text("name")
    val avatar = text("avatar").nullable()
    val stuffType = text("stuff_type").nullable()
    val grade = text("grade").nullable()
    val departmentParent = text("department_parent").nullable()
    val department = text("department").nullable()
    val email = text("email").nullable()
    val lastUpdate = timestamp("last_update")

    override val primaryKey = PrimaryKey(id)
}
