package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.review.LessonTimesReview
import com.mospolytech.domain.schedule.model.schedule.ScheduleDay
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceFull
import com.mospolytech.domain.schedule.model.source.ScheduleSources

interface ScheduleRepository {
    suspend fun getSchedule(): List<ScheduleDay>
    suspend fun getSchedule(source: ScheduleSource): List<ScheduleDay>

    suspend fun getSchedulePack(source: ScheduleSource): CompactSchedule
    suspend fun getSchedulePack(): CompactSchedule

    suspend fun getSourceList(sourceType: ScheduleSources): List<ScheduleSourceFull>
    suspend fun getLessonsReview(source: ScheduleSource): List<LessonTimesReview>
}