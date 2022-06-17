package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.base.utils.time
import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.lesson.toDateTimeRanges
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.place.PlaceDailyOccupancy
import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.place.PlaceOccupancyTimeRange
import com.mospolytech.domain.schedule.repository.FreePlacesRepository
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.repository.PlacesRepository
import com.mospolytech.domain.schedule.utils.filterByPlaces
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.time.LocalTime

class FreePlacesRepositoryImpl(
    private val lessonsRepository: LessonsRepository,
    private val placesRepository: PlacesRepository
) : FreePlacesRepository {

    override suspend fun getPlaces(filters: PlaceFilters): Map<PlaceInfo, Int> {
        val lessons = lessonsRepository.getLessonsByPlaces(filters.ids)

        return arrangePlacesByLessons(lessons, filters.dateTimeFrom, filters.dateTimeTo)
            .mapKeys { placesRepository.get(it.key) }
            .filterKeys { it != null }
            .mapKeys { it.key as PlaceInfo }
            .mapValues { it.value.count() }
    }

    override suspend fun getPlaceOccupancy(placeId: String): List<PlaceDailyOccupancy> {
        val lessons = lessonsRepository.getLessonsByPlace(placeId).lessons

        val resMap = mutableMapOf<LocalDate, MutableList<TempPlaceOccupancyTimeRange>>()

        lessons.forEach { lessonAndTimes ->
            lessonAndTimes.times.forEach { lessonDateTime ->
                lessonDateTime.toDateTimeRanges().forEach last@ { dateTimeRange ->
                    val dailyOccupancy = resMap.getOrPut(dateTimeRange.start.date) { mutableListOf() }
                    val newTimeRange = TempPlaceOccupancyTimeRange(
                        timeFrom = dateTimeRange.start.time,
                        timeTo = dateTimeRange.endInclusive.time,
                        value = 1.0
                    )

                    // search for first timeRange where startTime is greater
                    val indexToInsert = dailyOccupancy.indexOfFirst {
                        it.timeFrom > newTimeRange.timeFrom
                    }

                    // just add in the end of list
                    // if timeRange with startTime is greater than our
                    // not found
                    if (indexToInsert == -1) {
                        dailyOccupancy.add(newTimeRange)
                        return@last
                    }

                    val itemToMove = dailyOccupancy[indexToInsert]

                    // if newTimeRange intersect next item (itemToMove)
                    if (newTimeRange.timeTo >= itemToMove.timeFrom ) {
                        itemToMove += newTimeRange
                        fixRemainingIntersectionsAfter(indexToInsert, dailyOccupancy)
                    } else {
                        dailyOccupancy.add(indexToInsert, newTimeRange)
                    }

                    fixRemainingIntersectionsBefore(indexToInsert, dailyOccupancy)
                }
            }
        }

        return resMap.map {
            PlaceDailyOccupancy(
                date = it.key,
                values = it.value.sortedBy { it.timeFrom }
                    .map {
                        PlaceOccupancyTimeRange(
                            timeFrom = it.timeFrom,
                            timeTo = it.timeTo,
                            value = it.value
                        )
                    }
            )
        }
    }

    private fun fixRemainingIntersectionsAfter(
        currentIndex: Int,
        dailyOccupancy: MutableList<TempPlaceOccupancyTimeRange>
    ) {
        val nextIndex = currentIndex + 1
        val currItem = dailyOccupancy[currentIndex]

        while (currentIndex != dailyOccupancy.lastIndex) {
            val nextItem = dailyOccupancy[nextIndex]

            if (nextItem.timeFrom <= currItem.timeTo) {
                currItem += nextItem
                dailyOccupancy.removeAt(nextIndex)
            } else {
                break
            }
        }
    }

    private fun fixRemainingIntersectionsBefore(
        currentIndex: Int,
        dailyOccupancy: MutableList<TempPlaceOccupancyTimeRange>
    ) {
        var prevIndex = currentIndex - 1
        val currItem = dailyOccupancy[currentIndex]

        while (prevIndex >= 0) {
            val prevItem = dailyOccupancy[prevIndex]

            if (prevItem.timeTo >= currItem.timeFrom) {
                currItem += prevItem
                dailyOccupancy.removeAt(prevIndex)
                prevIndex--
            } else {
                break
            }
        }
    }

    private operator fun TempPlaceOccupancyTimeRange.plusAssign(other: TempPlaceOccupancyTimeRange) {
        this.timeFrom = if (this.timeFrom < other.timeFrom) this.timeFrom else other.timeFrom
        this.timeTo = if (this.timeTo < other.timeTo) this.timeTo else other.timeTo
        this.value += other.value
    }

    private data class TempPlaceOccupancyTimeRange(
        var timeFrom: LocalTime,
        var timeTo: LocalTime,
        var value: Double
    )

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