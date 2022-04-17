package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.model.place.PlaceInfo

interface FreePlacesRepository {
    suspend fun getPlaces(filters: PlaceFilters): Map<PlaceInfo, List<CompactLessonAndTimes>>
}