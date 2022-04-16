package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.schedule.model.group.GroupInfo
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.teacher.TeacherInfo
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.repository.ScheduleInfoRepository

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