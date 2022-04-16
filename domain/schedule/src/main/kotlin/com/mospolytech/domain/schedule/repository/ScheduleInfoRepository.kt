package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.group.GroupInfo
import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.place.Place
import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.model.review.LessonTimesReview
import com.mospolytech.domain.schedule.model.schedule.ScheduleDay
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.pack.ScheduleInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceFull
import com.mospolytech.domain.schedule.model.source.ScheduleSources
import com.mospolytech.domain.schedule.model.teacher.TeacherInfo

interface ScheduleInfoRepository {
    suspend fun getTeacherInfo(id: String): Result<TeacherInfo>
    suspend fun getGroupInfo(id: String): Result<GroupInfo>
    suspend fun getPlaceInfo(id: String): Result<PlaceInfo>
    suspend fun getSubjectInfo(id: String): Result<LessonSubjectInfo>
    suspend fun getLessonTypeInfo(id: String): Result<LessonTypeInfo>
    suspend fun getStudentInfo(id: String): Result<String>
}