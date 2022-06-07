package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.schedule.model.group.GroupInfo
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.schedule.repository.*

class ScheduleInfoRepositoryImpl(
    private val teacherRepository: TeachersRepository,
    private val groupsRepository: GroupsRepository,
    private val placesRepository: PlacesRepository,
    private val lessonSubjectsRepository: LessonSubjectsRepository,
    private val lessonTypesRepository: LessonTypesRepository
) : ScheduleInfoRepository {
    override suspend fun getTeacher(id: String): Result<Teacher?> {
        return Result.success(teacherRepository.get(id))
    }

    override suspend fun getGroupInfo(id: String): Result<GroupInfo?> {
        return Result.success(groupsRepository.get(id))
    }

    override suspend fun getPlaceInfo(id: String): Result<PlaceInfo?> {
        return Result.success(placesRepository.get(id))
    }

    override suspend fun getSubjectInfo(id: String): Result<LessonSubjectInfo?> {
        return Result.success(lessonSubjectsRepository.get(id))
    }

    override suspend fun getLessonTypeInfo(id: String): Result<LessonTypeInfo?> {
        return Result.success(lessonTypesRepository.get(id))
    }

    override suspend fun getStudentInfo(id: String): Result<String?> {
        return Result.success("")
    }
}