package com.mospolytech.data.schedule

import com.mospolytech.data.schedule.converters.ApiScheduleConverter
import com.mospolytech.data.schedule.converters.buildSchedule
import com.mospolytech.data.schedule.converters.getDateRange
import com.mospolytech.data.schedule.converters.mergeLessons
import com.mospolytech.domain.schedule.model.*
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.domain.schedule.utils.filterByGroup
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
        val (minDate, maxDate) = getDateRange(lessons)

        return buildSchedule(lessons, minDate, maxDate)
    }

    override suspend fun getSchedule(source: ScheduleSource): List<ScheduleDay> {
        val lessons = getLessons()
        val (minDate, maxDate) = getDateRange(lessons)

        return when (source.type) {
            ScheduleSources.Group -> buildSchedule(lessons.filterByGroup(source.key), minDate, maxDate)
            ScheduleSources.Teacher -> buildSchedule(lessons, minDate, maxDate)
            ScheduleSources.Student -> buildSchedule(lessons, minDate, maxDate)
            ScheduleSources.Place -> buildSchedule(lessons, minDate, maxDate)
            ScheduleSources.Subject -> buildSchedule(lessons, minDate, maxDate)
            ScheduleSources.Complex -> buildSchedule(lessons, minDate, maxDate)
        }
    }

    override suspend fun getLessons(): List<LessonDateTimes> {
        return if (scheduleCacheUpdateDateTime.until(LocalDateTime.now(), ChronoUnit.HOURS) > 24) {
            updateSchedule()
        } else {
            scheduleCache
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