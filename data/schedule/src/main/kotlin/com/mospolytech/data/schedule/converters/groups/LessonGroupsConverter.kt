package com.mospolytech.data.schedule.converters.groups

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.schedule.model.response.ApiGroup
import com.mospolytech.domain.peoples.utils.toGroupId
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import kotlin.collections.HashMap

class LessonGroupsConverter {
    private val converterCache = HashMap<String, GroupCache>()
    private val dbCache = HashMap<GroupCache, String>()

    private fun convertGroup(rawModel: Pair<String, Int>): GroupCache {
        val (title, course) = rawModel
        return converterCache.getOrPut(title) {
            GroupCache(
                title = title,
                course = course,
            )
        }
    }

    fun getCachedIds(rawGroups: List<ApiGroup>): List<String> {
        return rawGroups.map { getCachedId(it.title) }
    }

    private fun getCachedId(rawTitle: String): String {
        val dtoCache = checkNotNull(converterCache[rawTitle])
        return checkNotNull(dbCache[dtoCache])
    }

    suspend fun cacheAll(rawTitles: Set<Pair<String, Int>>) {
        MosPolyDb.transaction {
            val allDbItems = GroupsDb.selectAll().map { cacheDb(it) }.toSet()

            val dtoList = rawTitles.map { convertGroup(it) }

            val notInDb = mutableListOf<GroupCache>()
            val needUpdateDb = mutableListOf<GroupCache>()

            dtoList.forEach { dbCacheKey ->
                val dtoCacheKey = allDbItems.firstOrNull { dbCacheKey == it }
                if (dtoCacheKey == null) {
                    notInDb.add(dbCacheKey)
                } else if (dbCacheKey.course != dtoCacheKey.course) {
                    needUpdateDb.add(dbCacheKey)
                }
            }

            val rows =
                GroupsDb.batchInsert(notInDb) { dto ->
                    this[GroupsDb.id] = dto.title.toGroupId()
                    this[GroupsDb.title] = dto.title
                    this[GroupsDb.course] = dto.course
                }

            rows.forEach { cacheDb(it) }

            for (dto in needUpdateDb) {
                GroupsDb.update({ GroupsDb.id eq dto.title.toGroupId() }) {
                    it[GroupsDb.course] = dto.course
                }
            }
        }
    }

    private fun cacheDb(row: ResultRow): GroupCache {
        val id = row[GroupsDb.id].value
        val model =
            GroupCache(
                title = row[GroupsDb.title],
            )
        dbCache[model] = id
        return model
    }

    fun clearCache() {
        converterCache.clear()
        dbCache.clear()
    }

    private class GroupCache(
        val title: String,
        val course: Int? = null,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as GroupCache

            return title == other.title
        }

        override fun hashCode(): Int {
            return title.hashCode()
        }
    }
}
