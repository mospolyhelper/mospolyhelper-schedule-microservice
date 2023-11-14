package com.mospolytech.data.schedule.repository

import com.mospolytech.data.base.createPagingDto
import com.mospolytech.data.base.findOrAllIfEmpty
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.data.schedule.model.entity.SubjectEntity
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.schedule.model.lessonSubject.LessonSubjectInfo
import com.mospolytech.domain.schedule.repository.LessonSubjectsRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.mapLazy
import java.util.*

class LessonSubjectsRepositoryImpl : LessonSubjectsRepository {
    override suspend fun get(id: String): LessonSubjectInfo? {
        return MosPolyDb.transaction {
            SubjectEntity.findById(id)?.toModel()
        }
    }

    override suspend fun getPaging(
        query: String,
        pageSize: Int,
        page: Int,
    ): PagingDTO<LessonSubjectInfo> {
        return MosPolyDb.transaction {
            createPagingDto(pageSize, page) { offset ->
                SubjectEntity.findOrAllIfEmpty(query) { SubjectsDb.title like "%$query%" }
                    .orderBy(SubjectsDb.title to SortOrder.ASC)
                    .limit(pageSize, offset.toLong())
                    .mapLazy { it.toModel() }
                    .toList()
            }
        }
    }
}
