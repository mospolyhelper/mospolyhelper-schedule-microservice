package com.mospolytech.data.schedule.model.entity

import com.mospolytech.data.schedule.model.db.PlacesDb
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
}
