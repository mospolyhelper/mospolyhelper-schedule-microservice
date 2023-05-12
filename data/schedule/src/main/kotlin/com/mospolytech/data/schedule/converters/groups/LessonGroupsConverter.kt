package com.mospolytech.data.schedule.converters.groups

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.schedule.model.response.ApiGroup
import com.mospolytech.domain.peoples.model.Group
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import java.util.*
import kotlin.collections.HashMap

class LessonGroupsConverter {
    private val converterCache = HashMap<String, Group>()
    private val dbCache = HashMap<Group, String>()

    private fun convertGroup(rawTitle: String): Group {
        return converterCache.getOrPut(rawTitle) {
            Group(
                id = "",
                title = rawTitle,
                course = null,
                faculty = null,
                direction = null,
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

            val rows = GroupsDb.batchInsert(notInDb) { dto ->
                this[GroupsDb.id] = UUID.randomUUID().toString()
                this[GroupsDb.title] = dto.title
            }

            rows.forEach { cacheDb(it) }
        }
    }

    private fun cacheDb(row: ResultRow): Group {
        val id = row[GroupsDb.id].toString()
        val model = Group(
            id = "",
            title = row[GroupsDb.title],
            course = null,
            faculty = null,
            direction = null,
        )
        dbCache[model] = id
        return model
    }

    fun clearCache() {
        converterCache.clear()
        dbCache.clear()
    }
}
