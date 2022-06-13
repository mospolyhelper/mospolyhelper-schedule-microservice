package com.mospolytech.data.schedule.repository

import com.mospolytech.data.schedule.converters.ApiScheduleConverter
import com.mospolytech.data.schedule.converters.mergeLessons
import com.mospolytech.data.schedule.local.ScheduleCacheDS
import com.mospolytech.data.schedule.service.ScheduleService
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.utils.filterByPlaces
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class LessonsRepositoryImpl(
    private val service: ScheduleService,
    private val converter: ApiScheduleConverter,
    private val cacheDS: ScheduleCacheDS
) : LessonsRepository {
    private val updateScheduleLock = Any()

    private suspend fun updateSchedule(): List<CompactLessonAndTimes> {
        val semester = service.getSchedules()
        val lessonsSemester = converter.convertToLessons(semester)

        val session = service.getSchedulesSession()
        val lessonsSession = converter.convertToLessons(session)

        val mergedLessons = mergeLessons(lessonsSemester, lessonsSession)

        synchronized(updateScheduleLock) {
            cacheDS.scheduleCache = mergedLessons
            cacheDS.scheduleCacheUpdateDateTime = LocalDateTime.now()
        }
        return mergedLessons
    }

    override suspend fun getLessons(): List<CompactLessonAndTimes> {
        return if (cacheDS.scheduleCacheUpdateDateTime.until(LocalDateTime.now(), ChronoUnit.HOURS) > 24) {
            updateSchedule()
        } else {
            cacheDS.scheduleCache
        }
    }

    override suspend fun getLessonsByPlaces(placeIds: List<String>): List<CompactLessonAndTimes> {
        return getLessons()
            .let { if (placeIds.isNotEmpty()) it.filterByPlaces(placeIds) else it }
    }
}