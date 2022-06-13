package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.schedule.model.db.LessonTypesDb
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class LessonTypeEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<LessonTypeEntity>(LessonTypesDb)

    var title by LessonTypesDb.title
    var shortTitle by LessonTypesDb.shortTitle
    var description by LessonTypesDb.description
    var isImportant by LessonTypesDb.isImportant


    fun toModel(): LessonTypeInfo {
        return LessonTypeInfo(
            id = id.value.toString(),
            title = title,
            shortTitle = shortTitle,
            description = description,
            isImportant = isImportant
        )
    }
}