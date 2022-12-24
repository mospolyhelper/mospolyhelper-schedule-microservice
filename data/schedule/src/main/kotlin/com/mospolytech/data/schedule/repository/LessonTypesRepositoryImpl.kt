package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.LessonTypesDb
import com.mospolytech.data.schedule.model.entity.LessonTypeEntity
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.repository.LessonTypesRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.mapLazy
import java.util.*

class LessonTypesRepositoryImpl : LessonTypesRepository {
    private val map = mutableMapOf<String, String>()

    override suspend fun add(
        title: String,
        shortTitle: String,
        description: String,
        isImportant: Boolean,
    ): String {
        return MosPolyDb.transaction {
            var id = map[title]

            if (id == null) {
                id = LessonTypeEntity.find { LessonTypesDb.title eq title }
                    .mapLazy { it.toModel() }
                    .firstOrNull()
                    ?.id
            }

            if (id == null) {
                id = LessonTypeEntity.new {
                    this.title = title
                    this.shortTitle = shortTitle
                    this.description = description
                    this.isImportant = isImportant
                }.toModel().id
            }

            map[title] = id

            id
        }
    }

    override suspend fun get(id: String): LessonTypeInfo? {
        return MosPolyDb.transaction {
            LessonTypeEntity.findById(UUID.fromString(id))?.toModel()
        }
    }

    override suspend fun getAll(): List<LessonTypeInfo> {
        return MosPolyDb.transaction {
            LessonTypeEntity.all()
                .orderBy(LessonTypesDb.title to SortOrder.ASC)
                .map { it.toModel() }
        }
    }
}
