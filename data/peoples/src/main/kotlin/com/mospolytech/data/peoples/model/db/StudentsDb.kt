package com.mospolytech.data.peoples.model.db

import com.mospolytech.data.peoples.model.db.TeachersDb.index
import com.mospolytech.data.peoples.model.db.TeachersDb.nullable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object StudentsDb : IdTable<String>() {
    override val id: Column<EntityID<String>> = text("id").entityId()
    val lkId = TeachersDb.text("lk_id").nullable().index()
    val name = text("name")
    val birthday = date("birthday").nullable()
    val avatar = text("avatar").nullable()
    val group = reference("group", GroupsDb).nullable()
    val faculty = text("faculty").nullable()
    val direction = text("direction").nullable()
    val specialization = text("specialization").nullable()
    val educationType = text("education_type").nullable()
    val educationForm = text("education_form").nullable()
    val payment = text("payment").nullable()
    val course = integer("course").nullable()
    val years = text("years").nullable()
    val code = text("code").nullable()
    val dormitory = text("dormitory").nullable()
    val dormitoryRoom = text("dormitory_room").nullable()
    val branch = text("branch").nullable()
    val lastUpdate = timestamp("last_update")

    override val primaryKey = PrimaryKey(id)
}
