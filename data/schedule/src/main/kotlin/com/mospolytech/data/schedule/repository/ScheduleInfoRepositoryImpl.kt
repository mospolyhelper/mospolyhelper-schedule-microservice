package com.mospolytech.data.schedule.repository

import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.peoples.model.db.StudentsDb
import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.data.peoples.model.entity.*
import com.mospolytech.data.schedule.model.db.LessonTypesDb
import com.mospolytech.data.schedule.model.db.PlacesDb
import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.data.schedule.model.entity.LessonTypeEntity
import com.mospolytech.data.schedule.model.entity.PlaceEntity
import com.mospolytech.data.schedule.model.entity.SubjectEntity
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.schedule.repository.*
import java.util.*

class ScheduleInfoRepositoryImpl : ScheduleInfoRepository {
    override suspend fun getTeacher(id: String): Result<Teacher?> {
        return kotlin.runCatching {
            TeacherSafeEntity.find { TeachersDb.id eq id }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getGroupInfo(id: String): Result<Group?> {
        return kotlin.runCatching {
            GroupEntity.find { GroupsDb.id eq id }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getPlaceInfo(id: String): Result<PlaceInfo?> {
        val uuid = UUID.fromString(id)
        return kotlin.runCatching {
            PlaceEntity.find { PlacesDb.id eq uuid }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getSubjectInfo(id: String): Result<LessonSubjectInfo?> {
        val uuid = UUID.fromString(id)
        return kotlin.runCatching {
            SubjectEntity.find { SubjectsDb.id eq uuid }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getLessonTypeInfo(id: String): Result<LessonTypeInfo?> {
        val uuid = UUID.fromString(id)
        return kotlin.runCatching {
            LessonTypeEntity.find { LessonTypesDb.id eq uuid }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getStudentInfo(id: String): Result<Student?> {
        return kotlin.runCatching {
            StudentSafeEntity.find { StudentsDb.id eq id }
                .map { it.toModel() }
                .firstOrNull()
        }
    }
}