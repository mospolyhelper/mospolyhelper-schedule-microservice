package com.mospolytech.data.peoples.model.db

import org.jetbrains.exposed.dao.id.IntIdTable

object Students : IntIdTable() {
    val guid = text("guid")
    val firstName = text("firstName")
    val secondName = text("secondName")
    val surname = text("surname").nullable()
    val sex = text("sex")
    val avatar = text("avatar").nullable()
    val birthday = text("birthday").nullable()
    val faculty = text("faculty")
    val direction = text("direction")
    val specialization = text("specialization").nullable()
    val educationType = text("educationType").nullable()
    val educationForm = text("educationForm").nullable()
    val payment = bool("payment")
    val course = integer("course").nullable()
    val group = text("group").nullable()
    val years = text("years").nullable()
    val dialogId = text("dialogId").nullable()
    val additionalInfo = text("additionalInfo").nullable()
}