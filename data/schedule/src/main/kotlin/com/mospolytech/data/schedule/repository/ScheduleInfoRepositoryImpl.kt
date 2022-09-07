package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.peoples.model.db.StudentsDb
import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.data.peoples.model.entity.*
import com.mospolytech.data.schedule.model.db.LessonTypesDb
import com.mospolytech.data.schedule.model.db.PlacesDb
import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.data.schedule.model.entity.*
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.peoples.model.description
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_subject.description
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.place.description
import com.mospolytech.domain.schedule.model.schedule_info.ScheduleObject
import com.mospolytech.domain.schedule.repository.*
import org.jetbrains.exposed.sql.mapLazy
import java.util.*

class ScheduleInfoRepositoryImpl : ScheduleInfoRepository {
    override suspend fun getSubjectInfo(id: String): Result<LessonSubjectInfo?> {
        val uuid = UUID.fromString(id)
        return MosPolyDb.transactionCatching {
            SubjectEntity.find { SubjectsDb.id eq uuid }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getLessonTypeInfo(id: String): Result<LessonTypeInfo?> {
        val uuid = UUID.fromString(id)
        return MosPolyDb.transactionCatching {
            LessonTypeEntity.find { LessonTypesDb.id eq uuid }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getTeacher(id: String): Result<Teacher?> {
        return MosPolyDb.transactionCatching {
            TeacherSafeEntity.find { TeachersDb.id eq id }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getGroupInfo(id: String): Result<Group?> {
        return MosPolyDb.transactionCatching {
            GroupEntity.find { GroupsDb.id eq id }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getPlaceInfo(id: String): Result<PlaceInfo?> {
        val uuid = UUID.fromString(id)
        return MosPolyDb.transactionCatching {
            PlaceEntity.find { PlacesDb.id eq uuid }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getStudentInfo(id: String): Result<Student?> {
        return MosPolyDb.transactionCatching {
            StudentSafeEntity.find { StudentsDb.id eq id }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getAllSubjects(): Result<List<ScheduleObject>> {
        return MosPolyDb.transactionCatching {
            SubjectEntity.all().mapLazy {
                ScheduleObject(
                    id = it.id.value.toString(),
                    title = it.title,
                    description = it.description,
                    avatar = null
                )
            }.toList()
        }
    }

    override suspend fun getAllLessonTypes(): Result<List<ScheduleObject>> {
        return MosPolyDb.transactionCatching {
            LessonTypeEntity.all().mapLazy {
                ScheduleObject(
                    id = it.id.value.toString(),
                    title = it.title,
                    description = it.description,
                    avatar = null
                )
            }.toList()
        }
    }

    override suspend fun getAllTeachers(): Result<List<ScheduleObject>> {
        return MosPolyDb.transactionCatching {
            TeacherSafeEntity.all().mapLazy {
                ScheduleObject(
                    id = it.id.value,
                    title = it.name,
                    description = it.description,
                    avatar = null
                )
            }.toList()
        }
    }

    override suspend fun getAllGroups(): Result<List<ScheduleObject>> {
        return MosPolyDb.transactionCatching {
            GroupEntity.all().mapLazy {
                ScheduleObject(
                    id = it.id.value,
                    title = it.title,
                    description = it.description,
                    avatar = null
                )
            }.toList()
        }
    }

    override suspend fun getAllPlaces(): Result<List<ScheduleObject>> {
        return MosPolyDb.transactionCatching {
            PlaceEntity.all().mapLazy {
                ScheduleObject(
                    id = it.id.value.toString(),
                    title = it.title,
                    description = it.description2,
                    avatar = null
                )
            }.toList()
        }
    }

    override suspend fun getAllStudents(): Result<List<ScheduleObject>> {
        return MosPolyDb.transactionCatching {
            StudentSafeEntity.all().mapLazy {
                ScheduleObject(
                    id = it.id.value,
                    title = it.fullName(),
                    description = it.description,
                    avatar = null
                )
            }.toList()
        }
    }
}
