package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.lesson.toDateTimeRanges
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.repository.FreePlacesRepository
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.repository.PlacesRepository
import com.mospolytech.domain.schedule.utils.filterByPlaces
import java.time.LocalDateTime

class FreePlacesRepositoryImpl(
    private val lessonsRepository: LessonsRepository,
    private val placesRepository: PlacesRepository
) : FreePlacesRepository {

    override suspend fun getPlaces(filters: PlaceFilters): Map<PlaceInfo, List<CompactLessonAndTimes>> {
        val ids = filters.ids
        val lessons = lessonsRepository.getLessons().let { if (ids != null) it.filterByPlaces(ids) else it }

        return arrangePlacesByLessons(lessons, filters.dateTimeFrom, filters.dateTimeTo)
            .mapKeys { placesRepository.get(it.key) }.filterKeys { it != null }.mapKeys { it as PlaceInfo }
    }

    private fun arrangePlacesByLessons(
        lessons: List<CompactLessonAndTimes>,
        dateTimeFrom: LocalDateTime,
        dateTimeTo: LocalDateTime
    ): Map<String, List<CompactLessonAndTimes>> {
        return lessons.flatMap { it.lesson.placesId }
            .toSortedSet()
            .associateWith { getLessonsForPlace(it, lessons, dateTimeFrom, dateTimeTo) }
    }

    private fun getLessonsForPlace(
        placeId: String,
        lessons: List<CompactLessonAndTimes>,
        dateTimeFrom: LocalDateTime,
        dateTimeTo: LocalDateTime
    ): List<CompactLessonAndTimes> {
        return lessons.filter { it.lesson.placesId.any { it == placeId } && it.times.any { it in dateTimeFrom..dateTimeTo } }
    }

    operator fun ClosedRange<LocalDateTime>.contains(lessonDateTime: LessonDateTime): Boolean {
        val lessonDateTimeRanges = lessonDateTime.toDateTimeRanges()

        return lessonDateTimeRanges.any {
            it.start in this || it.endInclusive in this
        }
    }
}