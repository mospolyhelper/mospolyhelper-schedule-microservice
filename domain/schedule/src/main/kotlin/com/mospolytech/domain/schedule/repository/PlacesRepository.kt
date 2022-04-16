package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.base.model.Location
import com.mospolytech.domain.schedule.model.place.PlaceInfo

interface PlacesRepository {
    fun Building(
        title: String,
        areaAlias: String? = null,
        street: String? = null,
        building: String? = null,
        floor: String? = null,
        auditorium: String? = null,
        location: Location? = null,
        description: Map<String, String>? = null
    ): PlaceInfo

    fun Online(
        title: String,
        url: String? = null,
        description: Map<String, String>? = null
    ): PlaceInfo

    fun Other(
        title: String,
        description: Map<String, String>? = null
    ): PlaceInfo

    fun Unclassified(
        title: String,
        description: Map<String, String>? = null
    ): PlaceInfo


    fun get(id: String): PlaceInfo?
}