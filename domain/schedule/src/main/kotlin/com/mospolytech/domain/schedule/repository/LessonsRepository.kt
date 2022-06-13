package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.place.PlaceFilters

interface LessonsRepository {
    suspend fun getLessons(): List<CompactLessonAndTimes>
    suspend fun getAllLessons(): CompactSchedule
    suspend fun getLessonsByPlaces(placeIds: List<String> = emptyList()): List<CompactLessonAndTimes>
}