package com.mospolytech.data.schedule.converters

import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo

object LessonTypeConverter {
    fun convertType(type: String, title: String): LessonTypeInfo {
        return fixType(type, title)
    }


    enum class LessonTypes(
        val info: LessonTypeInfo
    ) {
        CourseProject(
            LessonTypeInfo.create(
                title = "Курсовой проект",
                shortTitle = "Курсовой проект",
                description = "",
                isImportant = true
            )
        ),
        Exam(
            LessonTypeInfo.create(
                title = "Экзамен",
                shortTitle = "Экзамен",
                description = "",
                isImportant = true
            )
        ),
        Credit(
            LessonTypeInfo.create(
                title = "Зачёт",
                shortTitle = "Зачёт",
                description = "",
                isImportant = true
            )
        ),
        CreditWithMark(
            LessonTypeInfo.create(
                title = "Зачёт с оценкой",
                shortTitle = "Зачёт с оценкой",
                description = "",
                isImportant = true
            )
        ),
        ExaminationReview(
            LessonTypeInfo.create(
                title = "Экзаменационный просмотр",
                shortTitle = "Экз. просмотр",
                description = "",
                isImportant = true
            )
        ),
        ExaminationDepartmentalReview(
            LessonTypeInfo.create(
                title = "Экз. кафедральный просмотр",
                shortTitle = "Экзаменационный каф. просмотр",
                description = "",
                isImportant = true
            )
        ),
        Consultation(
            LessonTypeInfo.create(
                title = "Консультация",
                shortTitle = "Консультация",
                description = "",
                isImportant = false
            )
        ),
        LaboratoryWork(
            LessonTypeInfo.create(
                title = "Лабораторная работа",
                shortTitle = "Лаб. работа",
                description = "",
                isImportant = false
            )
        ),
        Practice(
            LessonTypeInfo.create(
                title = "Практика",
                shortTitle = "Практика",
                description = "",
                isImportant = false
            )
        ),
        Lecture(
            LessonTypeInfo.create(
                title = "Лекция",
                shortTitle = "Лекция",
                description = "",
                isImportant = false
            )
        ),
        KeyLecture(
            LessonTypeInfo.create(
                title = "Установочная лекция",
                shortTitle = "Установочная лекция",
                description = "",
                isImportant = false
            )
        ),
        LectureAndPracticeAndLaboratory(
            LessonTypeInfo.create(
                title = "Лекция, практика, лабораторная работа",
                shortTitle = "Лекц., практ., лаб.",
                description = "",
                isImportant = false
            )
        ),
        LectureAndPractice(
            LessonTypeInfo.create(
                title = "Лекция и практика",
                shortTitle = "Лекц. и практ.",
                description = "",
                isImportant = false
            )
        ),
        LectureAndLaboratory(
            LessonTypeInfo.create(
                title = "Лекция и лабораторная работа",
                shortTitle = "Лекц. и лаб.",
                description = "",
                isImportant = false
            )
        ),
        PracticeAndLaboratory(
            LessonTypeInfo.create(
                title = "Практика и лабораторная работа",
                shortTitle = "Практ. и лаб.",
                description = "",
                isImportant = false
            )
        ),
        Other(
            LessonTypeInfo.create(
                title = "Другое",
                shortTitle = "Другое",
                description = "",
                isImportant = false
            )
        )
    }

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

    private const val PRACTICE_SHORT = "Пр"
    private const val LECTURE_SHORT = "Лек"
    private const val LABORATORY_SHORT = "Лаб"

    private val regex = Regex("\\(.*?\\)")

    private fun fixType(type: String, lessonTitle: String): LessonTypeInfo {
        val fixedType = lessonParserPacks.firstOrNull { it.sourceGroupType.equals(type, true) }
        if (fixedType?.fixedType == LessonTypes.Other) {
            return fixOtherType(type, lessonTitle)
        }
        return fixedType?.fixedType?.info
            ?: LessonTypeInfo.create(
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
        return fixedType?.fixedType?.info ?: LessonTypeInfo.create(title = type, type, "", false)
    }

    private fun fixOtherType(type: String, lessonTitle: String): LessonTypeInfo {
        val res = regex.findAll(lessonTitle).joinToString { it.value }
        return if (res.isNotEmpty()) {
            findCombinedShortTypeOrNull(res) ?: LessonTypeInfo.create(type, type, "", false)
        } else {
            LessonTypeInfo.create(title = type, type, "", false)
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