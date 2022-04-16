package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import com.mospolytech.domain.schedule.model.place.Place
import com.mospolytech.domain.schedule.model.place.PlaceFilters

interface FreePlacesRepository {
    suspend fun getPlaces(filters: PlaceFilters): Map<Place, List<LessonDateTimes>>
}