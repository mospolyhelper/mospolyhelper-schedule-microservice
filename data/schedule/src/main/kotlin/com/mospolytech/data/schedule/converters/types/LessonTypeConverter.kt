package com.mospolytech.data.schedule.converters.types

import com.mospolytech.domain.schedule.model.lessonType.Importance
import com.mospolytech.domain.schedule.utils.toLessonTypeId

class LessonTypeConverter {
    private val converterCache = HashMap<Pair<String, String>, LessonTypeCache>()

    private fun convertType(
        rawType: String,
        rawTitle: String,
    ): LessonTypeCache {
        return converterCache.getOrPut(rawType to rawTitle) {
            fixType(rawType, rawTitle)
        }
    }

    fun getCachedId(
        rawType: String,
        rawTitle: String,
    ): Pair<String, Importance> {
        val dtoCache = checkNotNull(converterCache[rawType to rawTitle])
        return dtoCache.title.toLessonTypeId() to dtoCache.isImportant
    }

    fun cacheAll(rawTypeToTitles: Set<Pair<String, String>>) {
        rawTypeToTitles.map { convertType(it.first, it.second) }
    }

    fun clearCache() {
        converterCache.clear()
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
    ): LessonTypeCache {
        val fixedType = lessonParserPacks.firstOrNull { it.sourceGroupType.equals(type, true) }
        if (fixedType?.fixedType == LessonTypes.Other) {
            return fixOtherType(type, lessonTitle)
        }
        return fixedType?.fixedType?.toInfo()
            ?: LessonTypeCache(
                title = type,
                shortTitle = type,
                description = null,
                isImportant = Importance.Normal,
            )
    }

    private fun fixOtherType(
        type: String,
        lessonTitle: String,
    ): LessonTypeCache {
        val res = regex.findAll(lessonTitle).joinToString { it.value }
        return if (res.isNotEmpty()) {
            findCombinedShortTypeOrNull(res) ?: LessonTypeCache(
                title = type,
                shortTitle = type,
                description = null,
                isImportant = Importance.Normal,
            )
        } else {
            LessonTypeCache(
                title = type,
                shortTitle = type,
                description = null,
                isImportant = Importance.Normal,
            )
        }
    }

    private fun findCombinedShortTypeOrNull(type: String): LessonTypeCache? {
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

    data class LessonTypeCache(
        val title: String,
        val shortTitle: String,
        val description: String?,
        val isImportant: Importance,
    )
}
