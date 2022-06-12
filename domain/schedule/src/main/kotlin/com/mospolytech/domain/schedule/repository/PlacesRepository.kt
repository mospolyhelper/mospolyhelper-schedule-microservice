package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.base.model.Location
import com.mospolytech.domain.schedule.model.place.PlaceInfo

interface PlacesRepository {
    fun addBuilding(
        title: String,
        areaAlias: String? = null,
        street: String? = null,
        building: String? = null,
        floor: String? = null,
        auditorium: String? = null,
        location: Location? = null,
        description: String? = null
    ): PlaceInfo

    fun addOnline(
        title: String,
        url: String? = null,
        description: String? = null
    ): PlaceInfo

    fun addOther(
        title: String,
        description: String? = null
    ): PlaceInfo

    fun addUnclassified(
        title: String,
        description: String? = null
    ): PlaceInfo


    fun get(id: String): PlaceInfo?
}