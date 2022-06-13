package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.peoples.model.entity.GroupEntity
import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.schedule.repository.GroupsRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.mapLazy

class GroupsRepositoryImpl : GroupsRepository {
    private val map = mutableMapOf<String, String>()

    override suspend fun getOrPut(
        title: String,
        course: String,
        isEvening: Boolean
    ): String {
        return MosPolyDb.transaction {
            var id = map[title]

            if (id == null) {
                id = GroupEntity.find { GroupsDb.title eq title }
                    .mapLazy { it.toModel() }
                    .firstOrNull()
                    ?.id
            }

            if (id == null) {
                id = GroupEntity.new {
                    this.title = title
                    this.course = course.toIntOrNull()
                }.toModel().id
            }

            map[title] = id

            id
        }
    }

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