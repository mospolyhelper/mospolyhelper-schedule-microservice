package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.place.PlaceDailyOccupancy
import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.model.place.PlaceInfo

interface FreePlacesRepository {
    suspend fun getPlaces(filters: PlaceFilters): Map<PlaceInfo, Int>

    suspend fun getPlaceOccupancy(placeId: String): List<PlaceDailyOccupancy>
}
