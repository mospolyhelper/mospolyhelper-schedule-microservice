package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.place.PlaceInfo

interface PlacesRepository {
    suspend fun get(id: String): PlaceInfo?
    suspend fun getAll(): List<PlaceInfo>
}
