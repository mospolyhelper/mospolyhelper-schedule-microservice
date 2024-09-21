package com.mospolytech.data.schedule.repository

import com.mospolytech.data.base.createPagingDto
import com.mospolytech.data.base.findOrAllIfEmpty
import com.mospolytech.data.base.replace
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.PlacesDb
import com.mospolytech.data.schedule.model.entity.PlaceEntity
import com.mospolytech.domain.base.model.Coordinates
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.schedule.model.place.CompactPlaceInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.place.PlaceTypes
import com.mospolytech.domain.schedule.repository.PlacesRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.mapLazy
import java.util.*

class PlacesRepositoryImpl : PlacesRepository {
    override suspend fun get(id: String): PlaceInfo? {
        return MosPolyDb.transaction {
            PlaceEntity.findById(UUID.fromString(id))?.toModel()
        }
    }

    override suspend fun getPaging(
        query: String,
        pageSize: Int,
        page: Int,
    ): PagingDTO<PlaceInfo> {
        return MosPolyDb.transaction {
            createPagingDto(pageSize, page) { offset ->
                PlaceEntity.findOrAllIfEmpty(query) {
                    PlacesDb.title.replace(" ", "").lowerCase() like "%${query.lowercase()}%"
                }.orderBy(PlacesDb.type to SortOrder.ASC, PlacesDb.title to SortOrder.ASC)
                    .limit(pageSize, offset.toLong())
                    .mapLazy { it.toModel() }
                    .toList()
            }
        }
    }
}

fun PlaceEntity.toModel(): PlaceInfo {
    return when (type) {
        PlaceTypes.Building ->
            PlaceInfo.Building(
                id = id.value.toString(),
                title = title,
                areaAlias = areaAlias,
                street = street,
                building = building,
                floor = floor,
                auditorium = auditorium,
                coordinates =
                lat?.let { lat ->
                    lng?.let { lng ->
                        Coordinates(
                            lat = lat,
                            lng = lng,
                        )
                    }
                },
                description = description,
            )
        PlaceTypes.Online ->
            PlaceInfo.Online(
                id = id.value.toString(),
                title = title,
                url = url,
                description = description,
            )
        PlaceTypes.Other ->
            PlaceInfo.Other(
                id = id.value.toString(),
                title = title,
                description = description,
            )
    }
}

fun PlaceEntity.toCompactModel(): CompactPlaceInfo {
    return when (type) {
        PlaceTypes.Building ->
            CompactPlaceInfo.Building(
                id = id.value.toString(),
                title = title,
                coordinates =
                lat?.let { lat ->
                    lng?.let { lng ->
                        Coordinates(
                            lat = lat,
                            lng = lng,
                        )
                    }
                },
                description = description,
            )
        PlaceTypes.Online ->
            CompactPlaceInfo.Online(
                id = id.value.toString(),
                title = title,
                url = url,
                description = description,
            )
        PlaceTypes.Other ->
            CompactPlaceInfo.Other(
                id = id.value.toString(),
                title = title,
                description = description,
            )
    }
}
