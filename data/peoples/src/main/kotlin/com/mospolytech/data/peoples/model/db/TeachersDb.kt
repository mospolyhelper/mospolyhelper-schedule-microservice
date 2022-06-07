package com.mospolytech.data.peoples.model.db

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.date

object TeachersDb : IdTable<String>() {
    override val id: Column<EntityID<String>> = text("guid").entityId()
    val name = text("name")
    val avatar = text("avatar").nullable()
    val stuffType = text("stuff_type").nullable()
    val grade = text("grade").nullable()
    val departmentParent = reference("department_parent", DepartmentsDb).nullable()
    val department = reference("department", DepartmentsDb).nullable()
    val email = text("email").nullable()
    val sex = text("sex").nullable()
    val birthday = date("birthday").nullable()
    val dialogId = text("dialog_id").nullable()
}