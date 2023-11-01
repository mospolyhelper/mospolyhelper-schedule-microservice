package com.mospolytech.data.schedule.converters.subjects

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.data.schedule.model.entity.SubjectEntity
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.batchInsert

class LessonSubjectConverter {
    private val converterCache = HashMap<String, LessonSubjectInfo>()
    private val dbCache = HashMap<LessonSubjectInfo, String>()

    private fun convertTitle(rawTitle: String): LessonSubjectInfo {
        val title = rawTitle
        val type: String? = null
        val description = buildString {
            type?.let {
                append(type)
            }
        }

        return converterCache.getOrPut(rawTitle) {
            LessonSubjectInfo(
                id = "",
                title = title,
                type = type,
                description = description,
            )
        }
    }

    fun getCachedId(rawTitle: String): String {
        val dtoCache = checkNotNull(converterCache[rawTitle])
        return checkNotNull(dbCache[dtoCache])
    }

    suspend fun cacheAll(rawTitles: Set<String>) {
        MosPolyDb.transaction {
            val allDbItems = SubjectEntity.all().map { cacheDb(it) }.toSet()

            val dtoList = rawTitles.map { convertTitle(it) }

            val notInDb = dtoList subtract allDbItems

            val rows = SubjectsDb.batchInsert(notInDb) { dto ->
                this[SubjectsDb.title] = dto.title
                this[SubjectsDb.type] = dto.type
            }

            SubjectEntity.wrapRows(SizedCollection(rows)).forEach { cacheDb(it) }
        }
    }

    private fun cacheDb(entity: SubjectEntity): LessonSubjectInfo {
        val model = entity.toModel()
        val modelWithoutId = model.copy(id = "")
        dbCache[modelWithoutId] = model.id
        return modelWithoutId
    }

    fun clearCache() {
        converterCache.clear()
        dbCache.clear()
    }
}
