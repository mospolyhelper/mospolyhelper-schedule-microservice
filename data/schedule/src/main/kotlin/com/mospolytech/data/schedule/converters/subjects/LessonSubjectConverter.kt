package com.mospolytech.data.schedule.converters.subjects

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.data.schedule.model.entity.SubjectEntity
import com.mospolytech.domain.peoples.utils.toLessonSubjectId
import org.jetbrains.exposed.sql.batchInsert

class LessonSubjectConverter {
    private val converterCache = HashMap<String, LessonSubjectCache>()

    private val regexList =
        listOf(
            """,\s*п/г\s*(\d)""".toRegex(),
            """\s*(\d)гр""".toRegex(),
            """\s*\(\s*(\d)\s*подгруппа\)""".toRegex(),
            """\s*\(\s*(\d)-я\s*подгруппа\)""".toRegex(),
        )
    // Обрезать пробелы по краям

    private fun convertTitle(rawTitle: String): LessonSubjectCache {
        val matchedRegex = regexList.firstOrNull { it.containsMatchIn(rawTitle) }
        val subgroup = matchedRegex?.find(rawTitle)?.groups?.get(1)?.value?.toByteOrNull()

        val title =
            if (matchedRegex != null) {
                rawTitle.replace(matchedRegex, "")
            } else {
                rawTitle
            }

        return converterCache.getOrPut(rawTitle) {
            LessonSubjectCache(
                title = title,
                subgroup = subgroup,
            )
        }
    }

    fun getCachedId(rawTitle: String): Pair<String, Byte?> {
        val dtoCache = checkNotNull(converterCache[rawTitle])
        return dtoCache.title.toLessonSubjectId() to dtoCache.subgroup
    }

    suspend fun cacheAll(rawTitles: Set<String>) {
        MosPolyDb.transaction {
            val allDbItems = SubjectEntity.all().map { it.title }.toSet()

            val dtoList = rawTitles.map { convertTitle(it) }.map { it.title }

            val notInDbSet = dtoList subtract allDbItems

            SubjectsDb.batchInsert(notInDbSet) { dto ->
                this[SubjectsDb.id] = dto.toLessonSubjectId()
                this[SubjectsDb.title] = dto
            }
        }
    }

    fun clearCache() {
        converterCache.clear()
    }

    data class LessonSubjectCache(
        val title: String,
        val subgroup: Byte?,
    )
}
