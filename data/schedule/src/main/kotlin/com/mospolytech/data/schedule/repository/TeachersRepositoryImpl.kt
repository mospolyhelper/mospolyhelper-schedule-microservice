package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.data.peoples.model.entity.TeacherSafeEntity
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.schedule.repository.TeachersRepository
import org.jetbrains.exposed.sql.SortOrder

class TeachersRepositoryImpl : TeachersRepository {
    override suspend fun get(id: String): Teacher? {
        return MosPolyDb.transaction {
            TeacherSafeEntity.findById(id)?.toModel()
        }
    }

    override suspend fun getAll(): List<Teacher> {
        return MosPolyDb.transaction {
            TeacherSafeEntity.all()
                .orderBy(TeachersDb.name to SortOrder.ASC)
                .map { it.toModel() }
        }
    }
}
