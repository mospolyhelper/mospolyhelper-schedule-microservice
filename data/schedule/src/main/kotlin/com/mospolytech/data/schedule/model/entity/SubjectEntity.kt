package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.data.schedule.model.db.LessonTypesDb
import com.mospolytech.data.schedule.model.db.LessonTypesDb.uniqueIndex
import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonType
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

class SubjectEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SubjectEntity>(SubjectsDb)

    var title by SubjectsDb.title
    var type by SubjectsDb.type


    fun toModel(): LessonSubjectInfo {
        return LessonSubjectInfo(
            id = id.value.toString(),
            title = title,
            type = type
        )
    }
}