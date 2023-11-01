package com.mospolytech.data.schedule.repository

import com.mospolytech.data.base.createPagingDto
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.data.schedule.model.entity.SubjectEntity
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.repository.LessonSubjectsRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.mapLazy
import java.util.*

class LessonSubjectsRepositoryImpl : LessonSubjectsRepository {

    override suspend fun get(id: String): LessonSubjectInfo? {
        return MosPolyDb.transaction {
            SubjectEntity.findById(UUID.fromString(id))?.toModel()
        }
    }

    override suspend fun getAll(): List<LessonSubjectInfo> {
        return MosPolyDb.transaction {
            SubjectEntity.all()
                .orderBy(SubjectsDb.title to SortOrder.ASC)
                .map { it.toModel() }
        }
    }

    override suspend fun getPaging(query: String, pageSize: Int, page: Int): PagingDTO<LessonSubjectInfo> {
        return MosPolyDb.transaction {
            createPagingDto(pageSize, page) { offset ->
                SubjectEntity.find { SubjectsDb.title like query }
                    .orderBy(SubjectsDb.title to SortOrder.ASC)
                    .limit(pageSize, offset.toLong())
                    .mapLazy { it.toModel() }
                    .toList()
            }
        }
    }
}
