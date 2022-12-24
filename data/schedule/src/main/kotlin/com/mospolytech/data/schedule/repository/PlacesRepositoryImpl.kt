package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.PlacesDb
import com.mospolytech.data.schedule.model.entity.PlaceEntity
import com.mospolytech.domain.base.model.Location
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.place.PlaceTypes
import com.mospolytech.domain.schedule.repository.PlacesRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.mapLazy
import java.util.*

class PlacesRepositoryImpl : PlacesRepository {
    private val map = mutableMapOf<PlaceCacheKey, String>()

    override suspend fun addBuilding(
        title: String,
        areaAlias: String?,
        street: String?,
        building: String?,
        floor: String?,
        auditorium: String?,
        location: Location?,
        description: String?,
    ): String {
        return MosPolyDb.transaction {
            val key = PlaceCacheKey(
                title = title,
                url = null,
                type = PlaceTypes.Building,
            )

            var id = map[key]

            if (id == null) {
                id = PlaceEntity.find {
                    PlacesDb.title eq title and (PlacesDb.type eq PlaceTypes.Building)
                }
                    .mapLazy { it.toModel() }
                    .firstOrNull()
                    ?.id
            }

            if (id == null) {
                id = PlaceEntity.new {
                    this.title = title
                    this.type = PlaceTypes.Building
                    this.areaAlias = areaAlias
                    this.street = street
                    this.building = building
                    this.floor = floor
                    this.auditorium = auditorium
                    this.lat = location?.lat
                    this.lng = location?.lng
                    this.description = description
                }.toModel().id
            }

            map[key] = id

            id
        }
    }

    override suspend fun addOnline(
        title: String,
        url: String?,
        description: String?,
    ): String {
        return MosPolyDb.transaction {
            val key = PlaceCacheKey(
                title = title,
                url = url,
                type = PlaceTypes.Online,
            )

            var id = map[key]

            if (id == null) {
                id = PlaceEntity.find {
                    PlacesDb.title eq title and
                        (PlacesDb.type eq PlaceTypes.Online) and
                        (PlacesDb.url eq url)
                }
                    .mapLazy { it.toModel() }
                    .firstOrNull()
                    ?.id
            }

            if (id == null) {
                id = PlaceEntity.new {
                    this.title = title
                    this.type = PlaceTypes.Online
                    this.url = url
                    this.description = description
                }.toModel().id
            }

            map[key] = id

            id
        }
    }

    override suspend fun addOther(
        title: String,
        description: String?,
    ): String {
        return MosPolyDb.transaction {
            val key = PlaceCacheKey(
                title = title,
                url = null,
                type = PlaceTypes.Other,
            )

            var id = map[key]

            if (id == null) {
                id = PlaceEntity.find {
                    PlacesDb.title eq title and
                        (PlacesDb.type eq PlaceTypes.Other)
                }
                    .mapLazy { it.toModel() }
                    .firstOrNull()
                    ?.id
            }

            if (id == null) {
                id = PlaceEntity.new {
                    this.title = title
                    this.type = PlaceTypes.Other
                    this.description = description
                }.toModel().id
            }

            map[key] = id

            id
        }
    }

    override suspend fun addUnclassified(
        title: String,
        description: String?,
    ): String {
        return MosPolyDb.transaction {
            val key = PlaceCacheKey(
                title = title,
                url = null,
                type = PlaceTypes.Unclassified,
            )

            var id = map[key]

            if (id == null) {
                id = PlaceEntity.find {
                    PlacesDb.title eq title and
                        (PlacesDb.type eq PlaceTypes.Unclassified)
                }
                    .mapLazy { it.toModel() }
                    .firstOrNull()
                    ?.id
            }

            if (id == null) {
                id = PlaceEntity.new {
                    this.title = title
                    this.type = PlaceTypes.Unclassified
                    this.description = description
                }.toModel().id
            }

            map[key] = id

            id
        }
    }

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
