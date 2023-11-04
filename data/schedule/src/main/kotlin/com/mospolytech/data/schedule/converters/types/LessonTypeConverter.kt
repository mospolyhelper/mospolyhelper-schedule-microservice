package com.mospolytech.data.schedule.converters.types

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.LessonTypesDb
import com.mospolytech.data.schedule.model.entity.LessonTypeEntity
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.batchInsert

class LessonTypeConverter {
    private val converterCache = HashMap<Pair<String, String>, LessonTypeInfo>()
    private val dbCache = HashMap<LessonTypeInfo, String>()

    private fun convertType(
        rawType: String,
        rawTitle: String,
    ): LessonTypeInfo {
        return converterCache.getOrPut(rawType to rawTitle) {
            fixType(rawType, rawTitle)
        }
    }

    fun getCachedId(
        rawType: String,
        rawTitle: String,
    ): String {
        val dtoCache = checkNotNull(converterCache[rawType to rawTitle])
        return checkNotNull(dbCache[dtoCache])
    }

    suspend fun cacheAll(rawTypeToTitles: Set<Pair<String, String>>) {
        MosPolyDb.transaction {
            val allDbItems = LessonTypeEntity.all().map { cacheDb(it) }.toSet()

            val dtoList = rawTypeToTitles.map { convertType(it.first, it.second) }

            val notInDb = dtoList subtract allDbItems

            val rows =
                LessonTypesDb.batchInsert(notInDb) { dto ->
                    this[LessonTypesDb.title] = dto.title
                    this[LessonTypesDb.shortTitle] = dto.shortTitle
                    this[LessonTypesDb.description] = dto.description
                    this[LessonTypesDb.isImportant] = dto.isImportant
                }

            LessonTypeEntity.wrapRows(SizedCollection(rows)).forEach { cacheDb(it) }
        }
    }

    private fun cacheDb(entity: LessonTypeEntity): LessonTypeInfo {
        val model = entity.toModel()
        val modelWithoutId = model.copy(id = "")
        dbCache[modelWithoutId] = model.id
        return modelWithoutId
    }

    fun clearCache() {
        converterCache.clear()
        dbCache.clear()
    }

    internal class LessonTypeParserPack(
        val sourceGroupType: String,
        val sourceTeacherType: String,
        val fixedType: LessonTypes,
    )

    private val lessonParserPacks =
        listOf(
            LessonTypeParserPack("КП", "КП", LessonTypes.CourseProject),
            LessonTypeParserPack("Экзамен", "Экз", LessonTypes.Exam),
            LessonTypeParserPack("Зачет", "Зач", LessonTypes.Credit),
            LessonTypeParserPack("ЗСО", "ЗСО", LessonTypes.CreditWithMark), // !!!
            LessonTypeParserPack("ЭП", "ЭП", LessonTypes.ExaminationReview), // !!!
            LessonTypeParserPack("ЭКП", "ЭКП", LessonTypes.ExaminationDepartmentalReview), // !!!
            LessonTypeParserPack("Консультация", "Кон", LessonTypes.Consultation),
            LessonTypeParserPack("Лаб. работа", "Лаб", LessonTypes.LaboratoryWork),
            LessonTypeParserPack("Практика", "Пра", LessonTypes.Practice),
            LessonTypeParserPack("Лекция", "Лек", LessonTypes.Lecture),
            LessonTypeParserPack("Установочная лекция", "Уст", LessonTypes.KeyLecture),
            LessonTypeParserPack("Лекция+практика", "лек.+пр.", LessonTypes.LectureAndPractice),
            LessonTypeParserPack("Другое", "Дру", LessonTypes.Other),
        )

    private val regex = Regex("\\(.*?\\)")

    private fun fixType(
        type: String,
        lessonTitle: String,
    ): LessonTypeInfo {
        val fixedType = lessonParserPacks.firstOrNull { it.sourceGroupType.equals(type, true) }
        if (fixedType?.fixedType == LessonTypes.Other) {
            return fixOtherType(type, lessonTitle)
        }
        return fixedType?.fixedType?.toInfo()
            ?: LessonTypeInfo(
                id = "",
                title = type,
                shortTitle = type,
                description = "",
                isImportant = false,
            )
    }

    private fun fixOtherType(
        type: String,
        lessonTitle: String,
    ): LessonTypeInfo {
        val res = regex.findAll(lessonTitle).joinToString { it.value }
        return if (res.isNotEmpty()) {
            findCombinedShortTypeOrNull(res) ?: LessonTypeInfo(id = "", type, type, "", false)
        } else {
            LessonTypeInfo(id = "", title = type, type, "", false)
        }
    }

    private fun findCombinedShortTypeOrNull(type: String): LessonTypeInfo? {
        val lecture = type.contains(LECTURE_SHORT, true)
        val practise = type.contains(PRACTICE_SHORT, true)
        val lab = type.contains(LABORATORY_SHORT, true)
        return when {
            lecture && practise && lab -> LessonTypes.LectureAndPracticeAndLaboratory.toInfo()
            lecture && practise -> LessonTypes.LectureAndPractice.toInfo()
            lecture && lab -> LessonTypes.LectureAndLaboratory.toInfo()
            practise && lab -> LessonTypes.PracticeAndLaboratory.toInfo()
            lecture -> LessonTypes.Lecture.toInfo()
            practise -> LessonTypes.Practice.toInfo()
            lab -> LessonTypes.LaboratoryWork.toInfo()
            else -> null
        }
    }

    companion object {
        private const val PRACTICE_SHORT = "Пр"
        private const val LECTURE_SHORT = "Лек"
        private const val LABORATORY_SHORT = "Лаб"
    }
}
