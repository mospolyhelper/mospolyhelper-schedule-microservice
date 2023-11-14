package com.mospolytech.data.schedule.converters.places

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.PlacesDb
import com.mospolytech.data.schedule.model.entity.PlaceEntity
import com.mospolytech.data.schedule.model.response.ApiLesson
import com.mospolytech.data.schedule.repository.toModel
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.place.PlaceTypes
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import kotlin.collections.HashMap

class LessonPlacesConverter {
    private val converterCache = HashMap<String, PlaceInfo>()
    private val dbCache = HashMap<PlaceInfo, String>()

    private fun convertPlace(rawTitle: String): PlaceInfo {
        return converterCache.getOrPut(rawTitle) {
            processAuditorium(rawTitle, "")
        }
    }

    fun getCachedIds(rawPlaces: List<ApiLesson.Auditory>): List<String> {
        return rawPlaces.map { getCachedId(it.title) }
    }

    private fun getCachedId(rawTitle: String): String {
        val dtoCache = checkNotNull(converterCache[rawTitle])
        return checkNotNull(dbCache[dtoCache])
    }

    suspend fun cacheAll(rawTitles: Set<String>) {
        MosPolyDb.transaction {
            val allDbItems = PlaceEntity.all().map { cacheDb(it) }.toSet()

            val dtoList = rawTitles.map { convertPlace(it) }

            val notInDb = dtoList subtract allDbItems

            val rows = PlacesDb.batchInsert(notInDb) { insert(it) }

            PlaceEntity.wrapRows(SizedCollection(rows)).forEach { cacheDb(it) }
        }
    }

    private fun BatchInsertStatement.insert(dto: PlaceInfo) {
        when (dto) {
            is PlaceInfo.Building -> {
                this[PlacesDb.title] = dto.title
                this[PlacesDb.type] = PlaceTypes.Building
                this[PlacesDb.areaAlias] = dto.areaAlias
                this[PlacesDb.street] = dto.street
                this[PlacesDb.building] = dto.building
                this[PlacesDb.floor] = dto.floor
                this[PlacesDb.auditorium] = dto.auditorium
                this[PlacesDb.lat] = dto.coordinates?.lat
                this[PlacesDb.lng] = dto.coordinates?.lng
                this[PlacesDb.description] = dto.description
            }
            is PlaceInfo.Online -> {
                this[PlacesDb.title] = dto.title
                this[PlacesDb.type] = PlaceTypes.Online
                this[PlacesDb.url] = dto.url
                this[PlacesDb.description] = dto.description
            }
            is PlaceInfo.Other -> {
                this[PlacesDb.title] = dto.title
                this[PlacesDb.type] = PlaceTypes.Other
                this[PlacesDb.description] = dto.description
            }
        }
    }

    @Suppress("UnnecessaryVariable")
    private fun cacheDb(entity: PlaceEntity): PlaceInfo {
        val model = entity.toModel()
        val modelWithoutId =
            model.let {
                when (model) {
                    is PlaceInfo.Building -> model.copy(id = "")
                    is PlaceInfo.Online -> model.copy(id = "")
                    is PlaceInfo.Other -> model.copy(id = "")
                }
            }
        dbCache[modelWithoutId] = model.id
        return modelWithoutId
    }

    fun clearCache() {
        converterCache.clear()
        dbCache.clear()
    }

    private val regex = Regex("""href="(.*?)".*?>(.*?)<""")

    private fun processAuditorium(
        auditorium: String,
        url: String,
    ): PlaceInfo {
        val regGroups = regex.find(auditorium)?.groupValues
        val (url2, rawTitle0) =
            if (regGroups != null) {
                regGroups.getOrNull(1) to regGroups.getOrNull(2)
            } else {
                null to null
            }

        val parsedHtml = rawTitle0 ?: auditorium
        val rawTitle =
            parsedHtml.trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        val (rawTitle2, type) = parseEmoji(rawTitle)
        val title = processTitle(rawTitle2)

        val finalUrl = url.ifEmpty { url2 ?: "" }

        return parsePlace(title, finalUrl)
    }

    private fun processTitle(raw: String): String {
        return fixTitle(raw.trim('_', '-'))
    }

    private fun fixTitle(raw: String): String {
        return when (raw) {
            "3301а" -> "М3301а"
            "2202а" -> "Пр2202а"
            else -> raw
        }
    }

    private val emojis =
        listOf(
            "\uD83D\uDCF7" to "Вебинар", // 📷
            "\uD83C\uDFE0" to "LMS", // 🏠
            "\uD83D\uDCBB" to "Видеоконф.", // 💻
            "\uD83C\uDF10" to "Online курс", // 🌐
        )

    private fun parseEmoji(raw: String): Pair<String, String> {
        val emoji = emojis.firstOrNull { raw.contains(it.first) }
        return if (emoji == null) {
            raw.trim() to ""
        } else {
            raw.replace(emoji.first, "").trim() to emoji.second
        }
    }

    private fun parsePlace(
        place: String,
        url: String = "",
    ): PlaceInfo {
        return parserChain.firstNotNullOfOrNull {
            val matchResult = place.parseBy(patterns = it.patterns.toTypedArray())
            if (matchResult == null) {
                null
            } else {
                it.placeFactory(matchResult, listOf(url))
            }
        } ?: PlaceInfo.Other(
            id = "",
            title = place,
            description = null,
        )
    }

    private fun String.parseBy(vararg patterns: String): MatchResult? {
        return patterns.firstNotNullOfOrNull {
            val regex = Regex(it, RegexOption.IGNORE_CASE)
            regex.matchEntire(this)
        }
    }
}
