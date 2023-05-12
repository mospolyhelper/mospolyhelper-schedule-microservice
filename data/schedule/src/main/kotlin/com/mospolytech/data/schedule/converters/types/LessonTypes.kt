package com.mospolytech.data.schedule.converters.types

import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo

internal enum class LessonTypes {
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
    Other,
}

internal fun LessonTypes.toInfo(): LessonTypeInfo {
    return predefinedTypes[this] ?: LessonTypeInfo(
        id = "",
        title = "Другое",
        shortTitle = "Другое",
        description = "",
        isImportant = false,
    )
}

private val predefinedTypes = mapOf(
    LessonTypes.CourseProject to LessonTypeInfo(
        id = "",
        title = "Курсовой проект",
        shortTitle = "Курсовой проект",
        description = "",
        isImportant = true,
    ),
    LessonTypes.Exam to LessonTypeInfo(
        id = "",
        title = "Экзамен",
        shortTitle = "Экзамен",
        description = "",
        isImportant = true,
    ),
    LessonTypes.Credit to LessonTypeInfo(
        id = "",
        title = "Зачёт",
        shortTitle = "Зачёт",
        description = "",
        isImportant = true,
    ),
    LessonTypes.CreditWithMark to LessonTypeInfo(
        id = "",
        title = "Зачёт с оценкой",
        shortTitle = "Зачёт с оценкой",
        description = "",
        isImportant = true,
    ),
    LessonTypes.ExaminationReview to LessonTypeInfo(
        id = "",
        title = "Экзаменационный просмотр",
        shortTitle = "Экз. просмотр",
        description = "",
        isImportant = true,
    ),
    LessonTypes.ExaminationDepartmentalReview to LessonTypeInfo(
        id = "",
        title = "Экзаменационный кафедральный просмотр",
        shortTitle = "Экз. каф. просмотр",
        description = "",
        isImportant = true,
    ),
    LessonTypes.Consultation to LessonTypeInfo(
        id = "",
        title = "Консультация",
        shortTitle = "Консультация",
        description = "",
        isImportant = false,
    ),
    LessonTypes.LaboratoryWork to LessonTypeInfo(
        id = "",
        title = "Лабораторная работа",
        shortTitle = "Лаб. работа",
        description = "",
        isImportant = false,
    ),
    LessonTypes.Practice to LessonTypeInfo(
        id = "",
        title = "Практика",
        shortTitle = "Практика",
        description = "",
        isImportant = false,
    ),
    LessonTypes.Lecture to LessonTypeInfo(
        id = "",
        title = "Лекция",
        shortTitle = "Лекция",
        description = "",
        isImportant = false,
    ),
    LessonTypes.KeyLecture to LessonTypeInfo(
        id = "",
        title = "Установочная лекция",
        shortTitle = "Установочная лекция",
        description = "",
        isImportant = false,
    ),
    LessonTypes.LectureAndPracticeAndLaboratory to LessonTypeInfo(
        id = "",
        title = "Лекция, практика, лабораторная работа",
        shortTitle = "Лекц., практ., лаб.",
        description = "",
        isImportant = false,
    ),
    LessonTypes.LectureAndPractice to LessonTypeInfo(
        id = "",
        title = "Лекция и практика",
        shortTitle = "Лекц. и практ.",
        description = "",
        isImportant = false,
    ),
    LessonTypes.LectureAndLaboratory to LessonTypeInfo(
        id = "",
        title = "Лекция и лабораторная работа",
        shortTitle = "Лекц. и лаб.",
        description = "",
        isImportant = false,
    ),
    LessonTypes.PracticeAndLaboratory to LessonTypeInfo(
        id = "",
        title = "Практика и лабораторная работа",
        shortTitle = "Практ. и лаб.",
        description = "",
        isImportant = false,
    ),
    LessonTypes.Other to LessonTypeInfo(
        id = "",
        title = "Другое",
        shortTitle = "Другое",
        description = "",
        isImportant = false,
    ),
)
