package com.mospolytech.data.schedule

import com.mospolytech.data.schedule.converters.*
import com.mospolytech.domain.schedule.model.*
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.domain.schedule.utils.filterByGroup
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
            ScheduleSources.Group -> lessons.filterByGroup(source.key)
            ScheduleSources.Teacher -> lessons
            ScheduleSources.Student -> lessons
            ScheduleSources.Place -> lessons
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
                    .map { ScheduleSourceFull(sourceType, it.title, it.title, "", "") }
            }
            ScheduleSources.Teacher -> {
                getLessons()
                    .flatMap { it.lesson.teachers }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, "", it.name, "", "") }
            }
            ScheduleSources.Student -> {
                getLessons()
                    .map { it.lesson.title }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, "", it, "", "") }
            }
            ScheduleSources.Place -> {
                getLessons()
                    .flatMap { it.lesson.places }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, "", it.title, "", "") }
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

        val resMap = mutableMapOf<String, MutableMap<TempClass, MutableList<LocalDate>>>()


        lessons.forEach { lessonDateTimes ->
            lessonDateTimes.time.forEach { lessonDateTime ->
                val key = TempClass(
                    type = lessonDateTimes.lesson.type,
                    dayOfWeek = lessonDateTime.date.dayOfWeek,
                    lessonTime = lessonDateTime.time
                )
                resMap
                    .getOrPut(lessonDateTimes.lesson.title) { mutableMapOf() }
                    .getOrPut(key) { mutableListOf() }
                    .add(lessonDateTime.date)
            }
        }


        val resList = resMap.map { (title, map2) ->
            LessonTimesReview(
                lessonTitle = title,
                days = map2.map {  (tempClass, dateList) ->
                    if (dateList.size == 1) {
                        LessonReviewDay.Single(
                            lessonType = tempClass.type,
                            date = dateList.first(),
                            time = tempClass.lessonTime
                        )
                    } else {
                        val (dateFrom, dateTo) = getDateRange(dateList)
                        LessonReviewDay.Regular(
                            lessonType = tempClass.type,
                            dayOfWeek = tempClass.dayOfWeek,
                            time = tempClass.lessonTime,
                            dateFrom = dateFrom,
                            dateTo = dateTo
                        )
                    }
                }.sorted()
            )
        }.sortedBy { it.lessonTitle }

        return resList
    }

    data class TempClass(
        val type: String,
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