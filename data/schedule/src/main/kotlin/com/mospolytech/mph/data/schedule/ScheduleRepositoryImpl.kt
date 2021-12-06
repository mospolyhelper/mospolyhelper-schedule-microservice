package com.mospolytech.mph.data.schedule

import com.mospolytech.mph.data.schedule.converters.ApiScheduleConverter
import com.mospolytech.mph.data.schedule.converters.buildSchedule
import com.mospolytech.mph.data.schedule.converters.getDateRange
import com.mospolytech.mph.data.schedule.converters.mergeLessons
import com.mospolytech.mph.domain.schedule.model.*
import com.mospolytech.mph.domain.schedule.repository.ScheduleRepository
import com.mospolytech.mph.domain.schedule.utils.filterByGroup
import java.time.LocalDate
import java.util.*

class ScheduleRepositoryImpl(
    private val service: ScheduleService,
    private val converter: ApiScheduleConverter
) : ScheduleRepository {
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
        val semester = service.getSchedules(false)
        //val session = service.getSchedules(true)
        val lessons = converter.convertToLessons(semester)
        return mergeLessons(lessons)
    }
}