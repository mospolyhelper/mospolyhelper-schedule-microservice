package com.mospolytech.mph.domain.schedule.repository

import com.mospolytech.mph.domain.schedule.model.LessonDateTimes
import com.mospolytech.mph.domain.schedule.model.ScheduleDay
import com.mospolytech.mph.domain.schedule.model.ScheduleSource

interface ScheduleRepository {
    suspend fun getSchedule(): List<ScheduleDay>
    suspend fun getSchedule(source: ScheduleSource): List<ScheduleDay>
    suspend fun getLessons(): List<LessonDateTimes>
}