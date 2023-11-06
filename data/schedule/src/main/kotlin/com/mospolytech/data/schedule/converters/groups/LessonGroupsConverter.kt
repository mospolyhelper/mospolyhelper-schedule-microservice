package com.mospolytech.data.schedule.converters.groups

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.schedule.model.response.ApiGroup
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import java.util.*
import kotlin.collections.HashMap

class LessonGroupsConverter {
    private val converterCache = HashMap<String, GroupCache>()
    private val dbCache = HashMap<GroupCache, String>()

    private fun convertGroup(rawTitle: String): GroupCache {
        return converterCache.getOrPut(rawTitle) {
            GroupCache(
                id = "",
                title = rawTitle,
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

    suspend fun cacheAll(rawTitles: Set<String>) {
        MosPolyDb.transaction {
            val allDbItems = GroupsDb.selectAll().map { cacheDb(it) }.toSet()

            val dtoList = rawTitles.map { convertGroup(it) }

            val notInDb = dtoList subtract allDbItems

            val rows =
                GroupsDb.batchInsert(notInDb) { dto ->
                    this[GroupsDb.id] = UUID.randomUUID().toString()
                    this[GroupsDb.title] = dto.title
                }

            rows.forEach { cacheDb(it) }
        }
    }

    private fun cacheDb(row: ResultRow): GroupCache {
        val id = row[GroupsDb.id].value
        val model =
            GroupCache(
                id = "",
                title = row[GroupsDb.title],
            )
        dbCache[model] = id
        return model
    }

    fun clearCache() {
        converterCache.clear()
        dbCache.clear()
    }

    private data class GroupCache(
        val id: String,
        val title: String,
    )
}
