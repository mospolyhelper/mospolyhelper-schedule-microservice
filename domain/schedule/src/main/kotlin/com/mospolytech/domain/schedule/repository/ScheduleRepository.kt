package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.*

interface ScheduleRepository {
    suspend fun getSchedule(): List<ScheduleDay>
    suspend fun getSchedule(source: ScheduleSource): List<ScheduleDay>
    suspend fun getLessons(): List<LessonDateTimes>
    suspend fun getSourceList(sourceType: ScheduleSources): List<ScheduleSourceFull>
}