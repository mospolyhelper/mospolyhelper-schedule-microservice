package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.schedule.model.db.LessonDateTimesDb
import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.lesson.LessonTime
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
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
            time =
                LessonTime(
                    start = startTime,
                    end = endTime,
                ),
        )
    }
}
