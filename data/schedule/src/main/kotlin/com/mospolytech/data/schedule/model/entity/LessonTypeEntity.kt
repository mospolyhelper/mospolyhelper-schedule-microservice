package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.data.schedule.model.db.LessonTypesDb
import com.mospolytech.data.schedule.model.db.LessonTypesDb.uniqueIndex
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.schedule.model.lesson_type.LessonType
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
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