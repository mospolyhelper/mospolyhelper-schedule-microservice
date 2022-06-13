package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
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