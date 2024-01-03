package com.mospolytech.data.schedule.converters.teachers

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.TeachersDb
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import java.util.*
import kotlin.collections.HashMap

class LessonTeachersConverter {
    private val converterCache = HashMap<String, TeacherCache>()
    private val dbCache = HashMap<TeacherCache, String>()

    private fun convertTeacher(rawName: String): TeacherCache {
        return converterCache.getOrPut(rawName) {
            TeacherCache(
                name = rawName.fixName1(),
            )
        }
    }

    private fun convertTeachers(rawTeachers: String): List<TeacherCache> {
        return rawTeachers.split(", ").mapNotNull {
            if (it.isEmpty()) {
                null
            } else {
                convertTeacher(it)
            }
        }
    }

    fun getCachedIds(rawTeachers: String): List<String> {
        return rawTeachers.split(", ").mapNotNull {
            if (it.isEmpty()) {
                null
            } else {
                getCachedId(it)
            }
        }
    }

    private fun getCachedId(rawTeacher: String): String {
        val dtoCache = checkNotNull(converterCache[rawTeacher])
        return checkNotNull(dbCache[dtoCache])
    }

    suspend fun cacheAll(rawTeachers: Set<String>) {
        MosPolyDb.transaction {
            // toSet использует LinkedHashSet, который должен сохранять порядок при итерации
            val allDbItems =
                TeachersDb.selectAll()
                    .sortedBy { stuffTypeOrder[it[TeachersDb.stuffType]] ?: stuffTypeOrder.size }
                    .map { cacheDb(it) }.toSet()

            val dtoList = rawTeachers.flatMap { convertTeachers(it) }

            val notInDb = dtoList subtract allDbItems

            // Пытаемся найти с заменой `ё` и добавляем дополнительный ключ для найденных
            val notInDb2 =
                notInDb.filter { rawCacheKey ->
                    val fixedCacheKey = rawCacheKey.copy(name = rawCacheKey.name.fixName2())
                    if (fixedCacheKey in allDbItems) {
                        dbCache[rawCacheKey] = dbCache[fixedCacheKey]!!
                        false
                    } else {
                        true
                    }
                }

            // Пытаемся найти преподавателей без отчества в расписании, но с отчеством в БД
            val notInDb3 =
                notInDb2.filter { rawCacheKey ->
                    if (rawCacheKey.name.isProbablyName()) {
                        val newCacheKey = allDbItems.firstOrNull { it.name.startsWith(rawCacheKey.name) }
                        if (newCacheKey == null) {
                            true
                        } else {
                            dbCache[rawCacheKey] = dbCache[newCacheKey]!!
                            false
                        }
                    } else {
                        true
                    }
                }

            val rows =
                TeachersDb.batchInsert(notInDb3) { dto ->
                    this[TeachersDb.id] = UUID.randomUUID().toString()
                    this[TeachersDb.name] = dto.name
                    this[TeachersDb.lastUpdate] = Clock.System.now()
                }

            rows.forEach { cacheDb(it) }
        }
    }

    private val stuffTypeOrder =
        mapOf(
            "Профессорско-преподавательский состав" to 0,
            "Научный работник" to 1,
            "Научно-технический работник" to 2,
            "Учебно-вспомогательный персонал" to 3,
            "Административно-управленческий персонал" to 4,
            "Инной педагогический работник" to 5,
            "Прочий обслуживающий персонал" to 6,
            "Младший обслуживающий персонал" to 7,
        )

    private fun cacheDb(raw: ResultRow): TeacherCache {
        val id = raw[TeachersDb.id].value
        val model =
            TeacherCache(
                name = raw[TeachersDb.name],
            )
        dbCache.putIfAbsent(model, id)
        return model
    }

    fun clearCache() {
        converterCache.clear()
        dbCache.clear()
    }

    private fun String.fixName1(): String {
        return trim().replace("  ", " ")
    }

    /**
     * Фикс ФИО, который желаетельно использовать, как вторую проверку*
     */
    private fun String.fixName2(): String {
        return replace('ё', 'е')
    }

    private fun String.isProbablyName(): Boolean {
        return (
            lastOrNull()?.isDigit() == true ||
                contains("Вакансия", true) ||
                contains("ППС", true) ||
                contains("Зав.каф.", true)
        ).not()
    }

    data class TeacherCache(
        val name: String,
    )
}
