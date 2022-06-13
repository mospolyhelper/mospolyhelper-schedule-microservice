package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.place.PlaceFilters

interface LessonsRepository {
    suspend fun getLessons(): List<CompactLessonAndTimes>
    suspend fun getLessonsByPlaces(placeIds: List<String> = emptyList()): List<CompactLessonAndTimes>
}