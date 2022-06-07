package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.group.GroupInfo
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.peoples.model.Teacher

interface ScheduleInfoRepository {
    suspend fun getTeacher(id: String): Result<Teacher?>
    suspend fun getGroupInfo(id: String): Result<GroupInfo?>
    suspend fun getPlaceInfo(id: String): Result<PlaceInfo?>
    suspend fun getSubjectInfo(id: String): Result<LessonSubjectInfo?>
    suspend fun getLessonTypeInfo(id: String): Result<LessonTypeInfo?>
    suspend fun getStudentInfo(id: String): Result<String?>
}