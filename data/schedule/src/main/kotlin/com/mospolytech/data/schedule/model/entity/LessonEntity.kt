package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.peoples.model.entity.GroupEntity
import com.mospolytech.data.peoples.model.entity.TeacherEntity
import com.mospolytech.data.schedule.model.db.*
import com.mospolytech.domain.base.utils.Moscow
import com.mospolytech.domain.schedule.model.lessonType.Importance
import com.mospolytech.domain.schedule.model.pack.AttendeeType
import com.mospolytech.domain.schedule.model.pack.CompactLessonEvent
import com.mospolytech.domain.schedule.model.pack.LessonDateTime
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class LessonEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<LessonEntity>(LessonsDb)

    var type by LessonsDb.type
    var subgroup by LessonsDb.subgroup
    var subject by SubjectEntity referencedOn LessonsDb.subject
    var teachers by TeacherEntity via LessonToTeachersDb
    var groups by GroupEntity via LessonToGroupsDb
    var places by PlaceEntity via LessonToPlacesDb
    val startDateTime by LessonsDb.startDateTime
    val endDateTime by LessonsDb.endDateTime
    val recurrence by RecurrenceEntity referrersOn RecurrenceDb.lesson
    val importance by LessonsDb.importance

    fun toModel(): CompactLessonEvent {
        val groupAttendees =
            groups.map {
                AttendeeType.Group.createFullId(it.id.toString())
            }
        val teacherAttendees =
            teachers.map {
                AttendeeType.Teacher.createFullId(it.id.toString())
            }
        val tags =
            if (subgroup != null) {
                listOf(type, "Подгруппа $subgroup")
            } else {
                listOf(type)
            }

        return CompactLessonEvent(
            id = id.value.toString(),
            tags = tags,
            subjectId = subject.id.toString(),
            placesId = places.map { it.id.toString() },
            attendeesId = groupAttendees + teacherAttendees,
            start =
            LessonDateTime(
                dateTime = startDateTime,
                timeZone = TimeZone.Moscow,
            ),
            end =
            LessonDateTime(
                dateTime = endDateTime,
                timeZone = TimeZone.Moscow,
            ),
            recurrence = recurrence.map { it.recurrence },
            importance = importance.toImportance(),
        )
    }
}

private fun Byte.toImportance(): Importance {
    return when {
        this < 0.toByte() -> Importance.Low
        this == 1.toByte() -> Importance.Normal
        else -> Importance.High
    }
}
