package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.data.schedule.model.db.LessonDateTimesDb
import com.mospolytech.data.schedule.model.db.LessonDateTimesDb.nullable
import com.mospolytech.data.schedule.model.db.LessonTypesDb
import com.mospolytech.data.schedule.model.db.LessonTypesDb.uniqueIndex
import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.lesson.LessonTime
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonType
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.time
import org.jetbrains.exposed.sql.kotlin.datetime.date
import java.util.*

class LessonDateTimeEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<LessonDateTimeEntity>(LessonDateTimesDb)

    var startDate by LessonDateTimesDb.startDate
    var endDate by LessonDateTimesDb.endDate
    var startTime by LessonDateTimesDb.startTime
    var endTime by LessonDateTimesDb.endTime


    fun toModel(): LessonDateTime {
        return LessonDateTime(
            startDate = startDate,
            endDate = endDate,
            time = LessonTime(
                start = startTime,
                end = endTime
            )
        )
    }
}