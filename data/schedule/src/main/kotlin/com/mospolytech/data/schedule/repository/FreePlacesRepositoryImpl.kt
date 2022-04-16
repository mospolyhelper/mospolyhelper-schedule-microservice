package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import com.mospolytech.domain.schedule.model.lesson.toDateTimeRanges
import com.mospolytech.domain.schedule.model.place.Place
import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.repository.FreePlacesRepository
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.utils.filterByPlaces
import java.time.LocalDateTime

class FreePlacesRepositoryImpl(
    private val lessonsRepository: LessonsRepository
) : FreePlacesRepository {

    override suspend fun getPlaces(filters: PlaceFilters): Map<Place, List<LessonDateTimes>> {
        val ids = filters.ids
        val lessons = lessonsRepository.getLessons().let { if (ids != null) it.filterByPlaces(ids) else it }

        return arrangePlacesByLessons(lessons, filters.dateTimeFrom, filters.dateTimeTo)
    }

    private fun arrangePlacesByLessons(
        lessons: List<LessonDateTimes>,
        dateTimeFrom: LocalDateTime,
        dateTimeTo: LocalDateTime
    ): Map<Place, List<LessonDateTimes>> {
        return lessons.flatMap { it.lesson.places }
            .toSortedSet()
            .associateWith { getLessonsForPlace(it, lessons, dateTimeFrom, dateTimeTo) }
    }

    private fun getLessonsForPlace(
        place: Place,
        lessons: List<LessonDateTimes>,
        dateTimeFrom: LocalDateTime,
        dateTimeTo: LocalDateTime
    ): List<LessonDateTimes> {
        return lessons.filter { it.lesson.places.any { it.id == place.id } && it.time.any { it in dateTimeFrom..dateTimeTo } }
    }

    operator fun ClosedRange<LocalDateTime>.contains(lessonDateTime: LessonDateTime): Boolean {
        val lessonDateTimeRanges = lessonDateTime.toDateTimeRanges()

        return lessonDateTimeRanges.any {
            it.start in this || it.endInclusive in this
        }
    }
}