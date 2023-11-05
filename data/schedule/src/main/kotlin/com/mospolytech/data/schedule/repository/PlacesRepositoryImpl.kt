package com.mospolytech.data.schedule.repository

import com.mospolytech.data.base.createPagingDto
import com.mospolytech.data.base.findOrAllIfEmpty
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.PlacesDb
import com.mospolytech.data.schedule.model.entity.PlaceEntity
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.repository.PlacesRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.mapLazy
import java.util.*

class PlacesRepositoryImpl : PlacesRepository {
    override suspend fun get(id: String): PlaceInfo? {
        return MosPolyDb.transaction {
            PlaceEntity.findById(UUID.fromString(id))?.toModel()
        }
    }

    override suspend fun getAll(): List<PlaceInfo> {
        return MosPolyDb.transaction {
            PlaceEntity.all()
                .orderBy(PlacesDb.type to SortOrder.ASC, PlacesDb.title to SortOrder.ASC)
                .map { it.toModel() }
        }
    }

    override suspend fun getPaging(
        query: String,
        pageSize: Int,
        page: Int,
    ): PagingDTO<PlaceInfo> {
        return MosPolyDb.transaction {
            createPagingDto(pageSize, page) { offset ->
                PlaceEntity.findOrAllIfEmpty(query) { PlacesDb.title like query }
                    .orderBy(PlacesDb.type to SortOrder.ASC, PlacesDb.title to SortOrder.ASC)
                    .limit(pageSize, offset.toLong())
                    .mapLazy { it.toModel() }
                    .toList()
            }
        }
    }
}
