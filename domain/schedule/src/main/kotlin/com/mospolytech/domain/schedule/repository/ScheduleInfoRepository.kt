package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.schedule.model.lessonSubject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lessonType.LessonTypeInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.scheduleInfo.ScheduleObject

interface ScheduleInfoRepository {
    suspend fun getTeacher(id: String): Result<Teacher?>

    suspend fun getGroupInfo(id: String): Result<Group?>

    suspend fun getPlaceInfo(id: String): Result<PlaceInfo?>

    suspend fun getSubjectInfo(id: String): Result<LessonSubjectInfo?>

    suspend fun getLessonTypeInfo(id: String): Result<LessonTypeInfo?>

    suspend fun getStudentInfo(id: String): Result<Student?>

    suspend fun getAllSubjects(): Result<List<ScheduleObject>>

    suspend fun getAllLessonTypes(): Result<List<ScheduleObject>>

    suspend fun getAllTeachers(): Result<List<ScheduleObject>>

    suspend fun getAllGroups(): Result<List<ScheduleObject>>

    suspend fun getAllPlaces(): Result<List<ScheduleObject>>

    suspend fun getAllStudents(): Result<List<ScheduleObject>>
}
