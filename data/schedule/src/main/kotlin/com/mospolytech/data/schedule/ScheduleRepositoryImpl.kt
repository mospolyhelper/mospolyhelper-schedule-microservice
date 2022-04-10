package com.mospolytech.data.schedule

import com.mospolytech.data.schedule.converters.*
import com.mospolytech.domain.schedule.model.group.GroupInfo
import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import com.mospolytech.domain.schedule.model.lesson.LessonTime
import com.mospolytech.domain.schedule.model.lesson.toDateTimeRange
import com.mospolytech.domain.schedule.model.lesson_type.LessonType
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.place.Place
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.model.review.LessonReviewDay
import com.mospolytech.domain.schedule.model.review.LessonTimesReview
import com.mospolytech.domain.schedule.model.review.LessonTimesReviewByType
import com.mospolytech.domain.schedule.model.schedule.ScheduleDay
import com.mospolytech.domain.schedule.model.schedule.ScheduleInfo
import com.mospolytech.domain.schedule.model.schedule.SchedulePack
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceFull
import com.mospolytech.domain.schedule.model.source.ScheduleSources
import com.mospolytech.domain.schedule.model.teacher.TeacherInfo
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.domain.schedule.utils.filterByGroup
import com.mospolytech.domain.schedule.utils.filterByPlace
import com.mospolytech.domain.schedule.utils.filterByPlaces
import com.mospolytech.domain.schedule.utils.filterByTeacher
import io.ktor.util.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ScheduleRepositoryImpl(
    private val service: ScheduleService,
    private val converter: ApiScheduleConverter
) : ScheduleRepository {
    private var scheduleCache: List<LessonDateTimes> = emptyList()
    private var scheduleCacheUpdateDateTime: LocalDateTime = LocalDateTime.MIN

    override suspend fun getSchedule(): List<ScheduleDay> {
        val lessons = getLessons()
        val (minDate, maxDate) = getLessonDateRange(lessons)

        return buildSchedule(lessons, minDate, maxDate)
    }

    override suspend fun getSchedule(source: ScheduleSource): List<ScheduleDay> {
        val lessons = getLessons(source)
        val (minDate, maxDate) = getLessonDateRange(lessons)
        return buildSchedule(lessons, minDate, maxDate)
    }

    override suspend fun getLessons(): List<LessonDateTimes> {
        return if (scheduleCacheUpdateDateTime.until(LocalDateTime.now(), ChronoUnit.HOURS) > 24) {
            updateSchedule()
        } else {
            scheduleCache
        }
    }

    suspend fun getLessons(source: ScheduleSource): List<LessonDateTimes> {
        val lessons = getLessons()

        return when (source.type) {
            ScheduleSources.Group -> lessons.filterByGroup(source.key.decodeBase64String())
            ScheduleSources.Teacher -> lessons.filterByTeacher(source.key.decodeBase64String())
            ScheduleSources.Student -> lessons
            ScheduleSources.Place -> lessons.filterByPlace(source.key.decodeBase64String())
            ScheduleSources.Subject -> lessons
            ScheduleSources.Complex -> lessons
        }
    }

    override suspend fun getSourceList(sourceType: ScheduleSources): List<ScheduleSourceFull> {
        return when (sourceType) {
            ScheduleSources.Group -> {
                getLessons()
                    .flatMap { it.lesson.groups }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.title, "", "") }
            }
            ScheduleSources.Teacher -> {
                getLessons()
                    .flatMap { it.lesson.teachers }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.name, "", "") }
            }
            ScheduleSources.Student -> {
                getLessons()
                    .flatMap { it.lesson.teachers }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.name, "", "") }
            }
            ScheduleSources.Place -> {
                getLessons()
                    .flatMap { it.lesson.places }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.title, "", "") }
            }
            ScheduleSources.Subject -> {
                getLessons()
                    .map { it.lesson.title }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, "", it, "", "") }
            }
            ScheduleSources.Complex -> emptyList()
        }
    }

    override suspend fun getLessonsReview(source: ScheduleSource): List<LessonTimesReview> {
        val lessons = getLessons(source)

        val resMap = mutableMapOf<String, MutableMap<LessonType, MutableMap<DayReviewUnit, MutableList<LocalDate>>>>()


        lessons.forEach { lessonDateTimes ->
            lessonDateTimes.time.forEach { lessonDateTime ->
                val key = DayReviewUnit(
                    dayOfWeek = lessonDateTime.date.dayOfWeek,
                    lessonTime = lessonDateTime.time
                )
                resMap
                    .getOrPut(lessonDateTimes.lesson.title) { mutableMapOf() }
                    .getOrPut(lessonDateTimes.lesson.type) { mutableMapOf() }
                    .getOrPut(key) { mutableListOf() }
                    .add(lessonDateTime.date)
            }
        }


        val resList = resMap.map { (title, typeToDays) ->
            LessonTimesReview(
                lessonTitle = title,
                days = typeToDays.map { (type, mapOfDays) ->
                    LessonTimesReviewByType(lessonType = type,
                       days = mapOfDays.map { (dayReviewUnit, dateList) ->
                           val (dateFrom, dateTo) = getDateRange(dateList)

                           LessonReviewDay(
                               dayOfWeek = dayReviewUnit.dayOfWeek,
                               time = dayReviewUnit.lessonTime,
                               dateFrom = dateFrom,
                               dateTo = dateTo
                           )
                       }.sorted()
                    )
                }.sorted()
            )
        }.sortedBy { it.lessonTitle }

        return resList
    }

    override suspend fun getPlaces(filters: PlaceFilters): Map<Place, List<LessonDateTimes>> {
        val ids = filters.ids
        val lessons = getLessons().let { if (ids != null) it.filterByPlaces(ids) else it }

        return arrangePlacesByLessons(lessons, filters.dateTimeFrom, filters.dateTimeTo)
    }

    override suspend fun getSchedulePack(source: ScheduleSource): SchedulePack {
        val lessons = getLessons(source)

        val typesId = lessons.asSequence().map { it.lesson.type.id }.distinct()
        val teachersId = lessons.asSequence().flatMap { it.lesson.teachers.map { it.id } }.distinct()
        val groupsId = lessons.asSequence().flatMap { it.lesson.groups.map { it.id } }.distinct()
        val placesId = lessons.asSequence().flatMap { it.lesson.places.map { it.id } }.distinct()

        return SchedulePack(
            lessons = lessons,
            info = ScheduleInfo(
                typesInfo = typesId.mapNotNull { LessonTypeInfo.map[it] }.toList(),
                teachersInfo = teachersId.mapNotNull { TeacherInfo.map[it] }.toList(),
                groupsInfo = groupsId.mapNotNull { GroupInfo.map[it] }.toList(),
                placesInfo = placesId.mapNotNull { PlaceInfo.map[it] }.toList()
            )
        )
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
        val lessonDateTimeRange = lessonDateTime.toDateTimeRange()

        return lessonDateTimeRange.start in this || lessonDateTimeRange.endInclusive in this
    }

    data class DayReviewUnit(
        val dayOfWeek: DayOfWeek,
        val lessonTime: LessonTime
    )

    private val updateScheduleLock = Any()

    private suspend fun updateSchedule(): List<LessonDateTimes> {
        val semester = service.getSchedules()
        val lessonsSemester = converter.convertToLessons(semester)

        val session = service.getSchedulesSession()

        val lessonsSession = converter.convertToLessons(session)

        val mergedLessons = mergeLessons(lessonsSemester, lessonsSession)
        synchronized(updateScheduleLock) {
            scheduleCache = mergedLessons
            scheduleCacheUpdateDateTime = LocalDateTime.now()
        }
        return mergedLessons
    }
}