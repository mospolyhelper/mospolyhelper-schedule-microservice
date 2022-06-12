package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.schedule.model.db.PlacesDb
import com.mospolytech.domain.base.model.Location
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.place.PlaceTypes
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PlaceEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PlaceEntity>(PlacesDb)

    var title by PlacesDb.title
    var type by PlacesDb.type
    var url by PlacesDb.url
    var areaAlias by PlacesDb.areaAlias
    var street by PlacesDb.street
    var building by PlacesDb.building
    var floor by PlacesDb.floor
    var auditorium by PlacesDb.auditorium
    var lat by PlacesDb.lat
    var lng by PlacesDb.lng
    var description by PlacesDb.description


    fun toModel(): PlaceInfo {
        return when (type) {
            PlaceTypes.Building -> PlaceInfo.Building(
                id = id.value.toString(),
                title = title,
                areaAlias = areaAlias,
                street = street,
                building = building,
                floor = floor,
                auditorium = auditorium,
                location = lat?.let { lat ->
                    lng?.let { lng ->
                        Location(
                            lat = lat,
                            lng = lng
                        )
                    }
                },
                description = description
            )
            PlaceTypes.Online -> PlaceInfo.Online(
                id = id.value.toString(),
                title = title,
                url = url,
                description = description
            )
            PlaceTypes.Other -> PlaceInfo.Other(
                id = id.value.toString(),
                title = title,
                description = description
            )
            PlaceTypes.Unclassified -> PlaceInfo.Unclassified(
                id = id.value.toString(),
                title = title,
                description = description
            )
        }
    }
}