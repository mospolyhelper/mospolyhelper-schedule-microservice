package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.schedule.model.lessonSubject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo

interface ScheduleInfoRepository {
    suspend fun getTeacher(id: String): Result<Teacher?>

    suspend fun getGroupInfo(id: String): Result<Group?>

    suspend fun getPlaceInfo(id: String): Result<PlaceInfo?>

    suspend fun getSubjectInfo(id: String): Result<LessonSubjectInfo?>

    suspend fun getStudentInfo(id: String): Result<Student?>
}
