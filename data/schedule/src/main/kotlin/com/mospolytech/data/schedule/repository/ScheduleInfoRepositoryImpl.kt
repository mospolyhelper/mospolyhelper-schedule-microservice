package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.peoples.model.db.StudentsDb
import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.data.peoples.model.entity.GroupEntity
import com.mospolytech.data.peoples.model.entity.StudentEntity
import com.mospolytech.data.peoples.model.entity.TeacherEntity
import com.mospolytech.data.schedule.model.db.PlacesDb
import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.data.schedule.model.entity.PlaceEntity
import com.mospolytech.data.schedule.model.entity.SubjectEntity
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.schedule.model.lessonSubject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.repository.ScheduleInfoRepository
import java.util.*

class ScheduleInfoRepositoryImpl : ScheduleInfoRepository {
    override suspend fun getSubjectInfo(id: String): Result<LessonSubjectInfo?> {
        return MosPolyDb.transactionCatching {
            SubjectEntity.find { SubjectsDb.id eq id }
                .map { it.toModel() }
                .firstOrNull()
        }
    }

    override suspend fun getTeacher(id: String): Result<Teacher?> {
        return MosPolyDb.transactionCatching {
            TeacherEntity.find { TeachersDb.id eq id }
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
            StudentEntity.find { StudentsDb.id eq id }
                .map { it.toModel() }
                .firstOrNull()
        }
    }
}
