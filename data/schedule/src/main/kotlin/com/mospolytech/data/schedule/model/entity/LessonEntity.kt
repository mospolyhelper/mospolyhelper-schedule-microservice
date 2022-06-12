package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.data.peoples.model.entity.GroupEntity
import com.mospolytech.data.peoples.model.entity.TeacherEntity
import com.mospolytech.data.peoples.model.entity.TeacherEntity.Companion.referrersOn
import com.mospolytech.data.schedule.model.db.*
import com.mospolytech.data.schedule.model.db.LessonTypesDb.uniqueIndex
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.schedule.model.lesson_type.LessonType
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.pack.CompactLessonFeatures
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

class LessonEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<LessonEntity>(LessonsDb)

    var type by LessonTypeEntity referencedOn LessonsDb.type
    var subject by SubjectEntity referencedOn LessonsDb.subject
    var teachers by TeacherEntity via LessonToTeachersDb
    var groups by GroupEntity via LessonToGroupsDb
    var places by PlaceEntity via LessonToPlacesDb
    var dateTimes by LessonDateTimeEntity via LessonToLessonDateTimesDb


    fun toModel(): CompactLessonFeatures {
        return CompactLessonFeatures(
            id = id.value.toString(),
            typeId = type.id.toString(),
            subjectId = subject.id.toString(),
            teachersId = teachers.map { it.id.toString() },
            groupsId = groups.map { it.id.toString() },
            placesId = places.map { it.id.toString() },
        )
    }
}