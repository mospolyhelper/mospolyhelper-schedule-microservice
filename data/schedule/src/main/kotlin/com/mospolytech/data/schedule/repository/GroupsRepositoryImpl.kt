package com.mospolytech.data.schedule.repository

import com.mospolytech.data.base.createPagingDto
import com.mospolytech.data.base.findOrAllIfEmpty
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.peoples.model.entity.GroupEntity
import com.mospolytech.data.peoples.model.entity.toShortModel
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.peoples.model.GroupShort
import com.mospolytech.domain.schedule.repository.GroupsRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.mapLazy

class GroupsRepositoryImpl : GroupsRepository {
    override suspend fun get(id: String): Group? {
        return MosPolyDb.transaction {
            GroupEntity.findById(id)?.toModel()
        }
    }

    override suspend fun getPagingShort(
        query: String,
        pageSize: Int,
        page: Int,
    ): PagingDTO<GroupShort> {
        return MosPolyDb.transaction {
            createPagingDto(pageSize, page) { offset ->
                GroupEntity.findOrAllIfEmpty(query) { GroupsDb.title like "%$query%" }
                    .orderBy(GroupsDb.title to SortOrder.ASC)
                    .limit(pageSize)
                    .offset(offset.toLong())
                    .mapLazy { it.toShortModel() }
                    .toList()
            }
        }
    }
}
