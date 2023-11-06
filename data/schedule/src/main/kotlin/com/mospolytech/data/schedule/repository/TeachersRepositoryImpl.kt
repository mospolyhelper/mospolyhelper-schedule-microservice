package com.mospolytech.data.schedule.repository

import com.mospolytech.data.base.createPagingDto
import com.mospolytech.data.base.findOrAllIfEmpty
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.data.peoples.model.entity.TeacherEntity
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.schedule.repository.TeachersRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.mapLazy

class TeachersRepositoryImpl : TeachersRepository {
    override suspend fun get(id: String): Teacher? {
        return MosPolyDb.transaction {
            TeacherEntity.findById(id)?.toModel()
        }
    }

    override suspend fun getAll(): List<Teacher> {
        return MosPolyDb.transaction {
            TeacherEntity.all()
                .orderBy(TeachersDb.name to SortOrder.ASC)
                .map { it.toModel() }
        }
    }

    override suspend fun getPaging(
        query: String,
        pageSize: Int,
        page: Int,
    ): PagingDTO<Teacher> {
        return MosPolyDb.transaction {
            createPagingDto(pageSize, page) { offset ->
                TeacherEntity.findOrAllIfEmpty(query) { TeachersDb.name like query }
                    .orderBy(TeachersDb.name to SortOrder.ASC)
                    .limit(pageSize, offset.toLong())
                    .mapLazy { it.toModel() }
                    .toList()
            }
        }
    }
}
