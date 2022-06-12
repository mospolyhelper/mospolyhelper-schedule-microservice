package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.base.model.Location
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.repository.PlacesRepository

class PlacesRepositoryImpl : PlacesRepository {
    private val map = mutableMapOf<String, PlaceInfo>()
    
    override fun addBuilding(
        title: String,
        areaAlias: String?,
        street: String?,
        building: String?,
        floor: String?,
        auditorium: String?,
        location: Location?,
        description: String?
    ): PlaceInfo {
        return PlaceInfo.Building(
            id = (PlaceInfo.Building::class.simpleName + title),
            title = title,
            areaAlias,
            street,
            building,
            floor,
            auditorium,
            location,
            description
        ).run {
            map.getOrPut(id) { this }
        }
    }

    override fun addOnline(
        title: String, 
        url: String?, 
        description: String?
    ): PlaceInfo {
        return PlaceInfo.Online(
            id = (PlaceInfo.Online::class.simpleName + title + url),
            title = title,
            url = url,
            description = description,
        ).run {
            map.getOrPut(id) { this }
        }
    }

    override fun addOther(
        title: String, 
        description: String?
    ): PlaceInfo {
        return PlaceInfo.Other(
            id = (PlaceInfo.Other::class.simpleName + title),
            title = title,
            description = description,
        ).run {
            map.getOrPut(id) { this }
        }
    }

    override fun addUnclassified(
        title: String, 
        description: String?
    ): PlaceInfo {
        return PlaceInfo.Unclassified(
            id = (PlaceInfo.Unclassified::class.simpleName + title),
            title = title,
            description = description,
        ).run {
            map.getOrPut(id) { this }
        }
    }

    override fun get(id: String): PlaceInfo? {
        return map[id]
    }
}