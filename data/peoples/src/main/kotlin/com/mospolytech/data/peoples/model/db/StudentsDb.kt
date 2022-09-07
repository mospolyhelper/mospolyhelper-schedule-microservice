package com.mospolytech.data.peoples.model.db

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.date

object StudentsDb : IdTable<String>() {
    override val id: Column<EntityID<String>> = text("id").entityId()
    val firstName = text("first_name")
    val lastName = text("last_name")
    val middleName = text("middle_name").nullable()
    val sex = text("sex").nullable()
    val birthday = date("birthday").nullable()
    val avatar = text("avatar").nullable()
    val group = reference("group", GroupsDb).nullable()
//    val faculty = reference("faculty", StudentFacultiesDb)
//    val direction = reference("direction", StudentDirectionsDb)
    val specialization = reference("specialization", StudentSpecializationsDb).nullable()
    val educationType = text("education_type")
    val educationForm = text("education_form")
    val payment = text("payment")
    val course = integer("course").nullable()
    val years = text("years")
    val code = text("code")
    val dormitory = text("dormitory").nullable()
    val dormitoryRoom = text("dormitory_room").nullable()
    val branch = reference("branch", StudentBranchesDb)

    override val primaryKey = PrimaryKey(id)
}
