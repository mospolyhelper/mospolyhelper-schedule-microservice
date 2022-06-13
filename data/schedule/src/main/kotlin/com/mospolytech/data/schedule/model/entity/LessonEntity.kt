package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.peoples.model.entity.GroupEntity
import com.mospolytech.data.peoples.model.entity.TeacherEntity
import com.mospolytech.data.schedule.model.db.*
import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.pack.CompactLessonFeatures
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class LessonEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<LessonEntity>(LessonsDb)

    var type by LessonTypeEntity referencedOn LessonsDb.type
    var subject by SubjectEntity referencedOn LessonsDb.subject
    var teachers by TeacherEntity via LessonToTeachersDb
    var groups by GroupEntity via LessonToGroupsDb
    var places by PlaceEntity via LessonToPlacesDb
    var dateTimes by LessonDateTimeEntity via LessonToLessonDateTimesDb


    fun toLessonModel(): CompactLessonFeatures {
        return CompactLessonFeatures(
            id = id.value.toString(),
            typeId = type.id.toString(),
            subjectId = subject.id.toString(),
            teachersId = teachers.map { it.id.toString() },
            groupsId = groups.map { it.id.toString() },
            placesId = places.map { it.id.toString() },
        )
    }

    fun toLessonTimeModel(): List<LessonDateTime> {
        return dateTimes.map {
            it.toModel()
        }
    }

    fun toFullModel(): CompactLessonAndTimes {
        return CompactLessonAndTimes(
            lesson = toLessonModel(),
            times = toLessonTimeModel()
        )
    }
}