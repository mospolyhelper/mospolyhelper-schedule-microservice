package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.peoples.model.entity.GroupEntity
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.schedule.repository.GroupsRepository
import org.jetbrains.exposed.sql.SortOrder

class GroupsRepositoryImpl : GroupsRepository {

    override suspend fun get(id: String): Group? {
        return MosPolyDb.transaction {
            GroupEntity.findById(id)?.toModel()
        }
    }

    override suspend fun getAll(): List<Group> {
        return MosPolyDb.transaction {
            GroupEntity.all()
                .orderBy(GroupsDb.title to SortOrder.ASC)
                .map { it.toModel() }
        }
    }
}
