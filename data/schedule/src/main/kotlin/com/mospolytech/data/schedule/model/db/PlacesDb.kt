package com.mospolytech.data.schedule.model.db

import com.mospolytech.domain.base.model.Location
import com.mospolytech.domain.schedule.model.place.PlaceTypes
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable

object PlacesDb : UUIDTable() {
    val title = text("title")
    val type = enumerationByName("type", 32, PlaceTypes::class)
    val areaAlias = text("area_alias").nullable()
    val street = text("street").nullable()
    val building = text("building").nullable()
    val floor = text("floor").nullable()
    val auditorium = text("auditorium").nullable()
    val lat = double("lat").nullable()
    val lng = double("lng").nullable()
    val url = text("auditorium").nullable()
    val description = text("description").nullable()

    init {
        uniqueIndex(title, type, url)
    }
}