package com.mospolytech.data.schedule.repository

import com.mospolytech.data.schedule.converters.*
import com.mospolytech.data.schedule.local.ScheduleCacheDS
import com.mospolytech.domain.schedule.model.group.GroupInfo
import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import com.mospolytech.domain.schedule.model.lesson.LessonTime
import com.mospolytech.domain.schedule.model.lesson.toDateTimeRanges
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonType
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.pack.CompactLessonFeatures
import com.mospolytech.domain.schedule.model.place.Place
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.model.review.LessonReviewDay
import com.mospolytech.domain.schedule.model.review.LessonTimesReview
import com.mospolytech.domain.schedule.model.review.LessonTimesReviewByType
import com.mospolytech.domain.schedule.model.schedule.ScheduleDay
import com.mospolytech.domain.schedule.model.pack.ScheduleInfo
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceFull
import com.mospolytech.domain.schedule.model.source.ScheduleSources
import com.mospolytech.domain.schedule.model.teacher.TeacherInfo
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.repository.ScheduleInfoRepository
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.domain.schedule.utils.*
import io.ktor.util.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ScheduleInfoRepositoryImpl(
    private val scheduleRepository: LessonsRepository
) : ScheduleInfoRepository {
    override suspend fun getTeacherInfo(id: String): Result<TeacherInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupInfo(id: String): Result<GroupInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlaceInfo(id: String): Result<PlaceInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getSubjectInfo(id: String): Result<LessonSubjectInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getLessonTypeInfo(id: String): Result<LessonTypeInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getStudentInfo(id: String): Result<String> {
        TODO("Not yet implemented")
    }
}