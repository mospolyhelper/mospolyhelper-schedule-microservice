package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.LessonTypesDb
import com.mospolytech.data.schedule.model.entity.LessonTypeEntity
import com.mospolytech.domain.schedule.model.lessonType.LessonTypeInfo
import com.mospolytech.domain.schedule.repository.LessonTypesRepository
import org.jetbrains.exposed.sql.SortOrder
import java.util.*

class LessonTypesRepositoryImpl : LessonTypesRepository {
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
