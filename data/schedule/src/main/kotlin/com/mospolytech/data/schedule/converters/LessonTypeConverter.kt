package com.mospolytech.data.schedule.converters

import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.repository.LessonTypesRepository

class LessonTypeConverter(
    private val lessonTypesRepository: LessonTypesRepository
) {
    fun convertType(type: String, title: String): LessonTypeInfo {
        return fixType(type, title)
    }


    enum class LessonTypes {
        CourseProject,
        Exam,
        Credit,
        CreditWithMark,
        ExaminationReview,
        ExaminationDepartmentalReview,
        Consultation,
        LaboratoryWork,
        Practice,
        Lecture,
        KeyLecture,
        LectureAndPracticeAndLaboratory,
        LectureAndPractice,
        LectureAndLaboratory,
        PracticeAndLaboratory,
        Other
    }

    private val LessonTypes.info
    get() = tempMap[this] ?: lessonTypesRepository.add(
        title = "Другое",
        shortTitle = "Другое",
        description = "",
        isImportant = false
    )

    private val tempMap = mapOf(
        LessonTypes.CourseProject to (
            lessonTypesRepository.add(
                title = "Курсовой проект",
                shortTitle = "Курсовой проект",
                description = "",
                isImportant = true
            )
        ),
        LessonTypes.Exam to (
            lessonTypesRepository.add(
                title = "Экзамен",
                shortTitle = "Экзамен",
                description = "",
                isImportant = true
            )
        ),
        LessonTypes.Credit to (
            lessonTypesRepository.add(
                title = "Зачёт",
                shortTitle = "Зачёт",
                description = "",
                isImportant = true
            )
        ),
        LessonTypes.CreditWithMark to (
            lessonTypesRepository.add(
                title = "Зачёт с оценкой",
                shortTitle = "Зачёт с оценкой",
                description = "",
                isImportant = true
            )
        ),
        LessonTypes.ExaminationReview to (
            lessonTypesRepository.add(
                title = "Экзаменационный просмотр",
                shortTitle = "Экз. просмотр",
                description = "",
                isImportant = true
            )
        ),
        LessonTypes. ExaminationDepartmentalReview to (
            lessonTypesRepository.add(
                title = "Экзаменационный кафедральный просмотр",
                shortTitle = "Экз. каф. просмотр",
                description = "",
                isImportant = true
            )
        ),
        LessonTypes.Consultation to (
            lessonTypesRepository.add(
                title = "Консультация",
                shortTitle = "Консультация",
                description = "",
                isImportant = false
            )
        ),
        LessonTypes.LaboratoryWork to (
            lessonTypesRepository.add(
                title = "Лабораторная работа",
                shortTitle = "Лаб. работа",
                description = "",
                isImportant = false
            )
        ),
        LessonTypes.Practice to (
            lessonTypesRepository.add(
                title = "Практика",
                shortTitle = "Практика",
                description = "",
                isImportant = false
            )
        ),
        LessonTypes.Lecture to (
            lessonTypesRepository.add(
                title = "Лекция",
                shortTitle = "Лекция",
                description = "",
                isImportant = false
            )
        ),
        LessonTypes.KeyLecture to (
            lessonTypesRepository.add(
                title = "Установочная лекция",
                shortTitle = "Установочная лекция",
                description = "",
                isImportant = false
            )
        ),
        LessonTypes.LectureAndPracticeAndLaboratory to(
            lessonTypesRepository.add(
                title = "Лекция, практика, лабораторная работа",
                shortTitle = "Лекц., практ., лаб.",
                description = "",
                isImportant = false
            )
        ),
        LessonTypes.LectureAndPractice to (
            lessonTypesRepository.add(
                title = "Лекция и практика",
                shortTitle = "Лекц. и практ.",
                description = "",
                isImportant = false
            )
        ),
        LessonTypes.LectureAndLaboratory to (
            lessonTypesRepository.add(
                title = "Лекция и лабораторная работа",
                shortTitle = "Лекц. и лаб.",
                description = "",
                isImportant = false
            )
        ),
        LessonTypes.PracticeAndLaboratory to (
            lessonTypesRepository.add(
                title = "Практика и лабораторная работа",
                shortTitle = "Практ. и лаб.",
                description = "",
                isImportant = false
            )
        ),
        LessonTypes.Other to (
            lessonTypesRepository.add(
                title = "Другое",
                shortTitle = "Другое",
                description = "",
                isImportant = false
            )
        )
    )

    class LessonTypeParserPack(
        val sourceGroupType: String,
        val sourceTeacherType: String,
        val fixedType: LessonTypes
    )

    private val lessonParserPacks = listOf(
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
        LessonTypeParserPack("Другое", "Дру", LessonTypes.Other)
    )

    private val PRACTICE_SHORT = "Пр"
    private val LECTURE_SHORT = "Лек"
    private val LABORATORY_SHORT = "Лаб"

    private val regex = Regex("\\(.*?\\)")

    private fun fixType(type: String, lessonTitle: String): LessonTypeInfo {
        val fixedType = lessonParserPacks.firstOrNull { it.sourceGroupType.equals(type, true) }
        if (fixedType?.fixedType == LessonTypes.Other) {
            return fixOtherType(type, lessonTitle)
        }
        return fixedType?.fixedType?.info
            ?: lessonTypesRepository.add(
                title = type,
                shortTitle = type,
                description = "",
                isImportant = false
            )
    }

    private fun fixTeacherType(type: String, lessonTitle: String): LessonTypeInfo {
        val fixedType = lessonParserPacks.firstOrNull { it.sourceTeacherType.equals(type, true) }
        if (fixedType?.fixedType == LessonTypes.Other) {
            return fixOtherType(type, lessonTitle)
        }
        return fixedType?.fixedType?.info ?: lessonTypesRepository.add(title = type, type, "", false)
    }

    private fun fixOtherType(type: String, lessonTitle: String): LessonTypeInfo {
        val res = regex.findAll(lessonTitle).joinToString { it.value }
        return if (res.isNotEmpty()) {
            findCombinedShortTypeOrNull(res) ?: lessonTypesRepository.add(type, type, "", false)
        } else {
            lessonTypesRepository.add(title = type, type, "", false)
        }
    }

    private fun findCombinedShortTypeOrNull(type: String): LessonTypeInfo? {
        val lecture = type.contains(LECTURE_SHORT, true)
        val practise = type.contains(PRACTICE_SHORT, true)
        val lab = type.contains(LABORATORY_SHORT, true)
        return when {
            lecture && practise && lab -> LessonTypes.LectureAndPracticeAndLaboratory.info
            lecture && practise -> LessonTypes.LectureAndPractice.info
            lecture && lab -> LessonTypes.LectureAndLaboratory.info
            practise && lab -> LessonTypes.PracticeAndLaboratory.info
            lecture -> LessonTypes.Lecture.info
            practise -> LessonTypes.Practice.info
            lab -> LessonTypes.LaboratoryWork.info
            else -> null
        }
    }
}