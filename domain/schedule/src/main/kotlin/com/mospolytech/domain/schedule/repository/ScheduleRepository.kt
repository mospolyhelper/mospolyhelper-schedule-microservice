package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import com.mospolytech.domain.schedule.model.place.Place
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.model.review.LessonTimesReview
import com.mospolytech.domain.schedule.model.schedule.ScheduleDay
import com.mospolytech.domain.schedule.model.schedule.SchedulePack
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceFull
import com.mospolytech.domain.schedule.model.source.ScheduleSources

interface ScheduleRepository {
    suspend fun getSchedule(): List<ScheduleDay>
    suspend fun getSchedule(source: ScheduleSource): List<ScheduleDay>
    suspend fun getLessons(): List<LessonDateTimes>
    suspend fun getSourceList(sourceType: ScheduleSources): List<ScheduleSourceFull>
    suspend fun getLessonsReview(source: ScheduleSource): List<LessonTimesReview>
    suspend fun getPlaces(filters: PlaceFilters): Map<Place, List<LessonDateTimes>>
    suspend fun getSchedulePack(source: ScheduleSource): SchedulePack
}