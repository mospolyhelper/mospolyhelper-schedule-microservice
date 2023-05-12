package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.PlacesDb
import com.mospolytech.data.schedule.model.entity.PlaceEntity
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.place.PlaceTypes
import com.mospolytech.domain.schedule.repository.PlacesRepository
import org.jetbrains.exposed.sql.SortOrder
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

    private data class PlaceCacheKey(
        val title: String,
        val url: String?,
        val type: PlaceTypes,
    )
}
