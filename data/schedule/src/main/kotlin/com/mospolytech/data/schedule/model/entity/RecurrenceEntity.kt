package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.schedule.model.db.RecurrenceDb
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class RecurrenceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RecurrenceEntity>(RecurrenceDb)

    var recurrence by RecurrenceDb.recurrence
    var lesson by RecurrenceDb.lesson
}
