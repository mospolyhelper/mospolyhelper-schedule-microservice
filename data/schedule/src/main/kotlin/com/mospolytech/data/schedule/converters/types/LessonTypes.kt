package com.mospolytech.data.schedule.converters.types

import com.mospolytech.domain.schedule.model.lessonType.Importance

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

internal fun LessonTypes.toInfo(): LessonTypeConverter.LessonTypeCache {
    return predefinedTypes[this] ?: LessonTypeConverter.LessonTypeCache(
        title = "Другое",
        shortTitle = "Другое",
        description = null,
        isImportant = Importance.Normal,
    )
}

private val predefinedTypes =
    mapOf(
        LessonTypes.CourseProject to
            createLessonType(
                title = "Курсовой проект",
                shortTitle = "Курсовой проект",
                description = null,
                isImportant = Importance.High,
            ),
        LessonTypes.Exam to
            createLessonType(
                title = "Экзамен",
                shortTitle = "Экзамен",
                description = null,
                isImportant = Importance.High,
            ),
        LessonTypes.Credit to
            createLessonType(
                title = "Зачёт",
                shortTitle = "Зачёт",
                description = null,
                isImportant = Importance.High,
            ),
        LessonTypes.CreditWithMark to
            createLessonType(
                title = "Зачёт с оценкой",
                shortTitle = "Зачёт с оценкой",
                description = null,
                isImportant = Importance.High,
            ),
        LessonTypes.ExaminationReview to
            createLessonType(
                title = "Экзаменационный просмотр",
                shortTitle = "Экз. просмотр",
                description = null,
                isImportant = Importance.High,
            ),
        LessonTypes.ExaminationDepartmentalReview to
            createLessonType(
                title = "Экзаменационный кафедральный просмотр",
                shortTitle = "Экз. каф. просмотр",
                description = null,
                isImportant = Importance.High,
            ),
        LessonTypes.Consultation to
            createLessonType(
                title = "Консультация",
                shortTitle = "Консультация",
                description = null,
                isImportant = Importance.Normal,
            ),
        LessonTypes.LaboratoryWork to
            createLessonType(
                title = "Лабораторная работа",
                shortTitle = "Лаб. работа",
                description = null,
                isImportant = Importance.Normal,
            ),
        LessonTypes.Practice to
            createLessonType(
                title = "Практика",
                shortTitle = "Практика",
                description = null,
                isImportant = Importance.Normal,
            ),
        LessonTypes.Lecture to
            createLessonType(
                title = "Лекция",
                shortTitle = "Лекция",
                description = null,
                isImportant = Importance.Normal,
            ),
        LessonTypes.KeyLecture to
            createLessonType(
                title = "Установочная лекция",
                shortTitle = "Установочная лекция",
                description = null,
                isImportant = Importance.Normal,
            ),
        LessonTypes.LectureAndPracticeAndLaboratory to
            createLessonType(
                title = "Лекция, практика, лабораторная работа",
                shortTitle = "Лекц., практ., лаб.",
                description = null,
                isImportant = Importance.Normal,
            ),
        LessonTypes.LectureAndPractice to
            createLessonType(
                title = "Лекция и практика",
                shortTitle = "Лекц. и практ.",
                description = null,
                isImportant = Importance.Normal,
            ),
        LessonTypes.LectureAndLaboratory to
            createLessonType(
                title = "Лекция и лабораторная работа",
                shortTitle = "Лекц. и лаб.",
                description = null,
                isImportant = Importance.Normal,
            ),
        LessonTypes.PracticeAndLaboratory to
            createLessonType(
                title = "Практика и лабораторная работа",
                shortTitle = "Практ. и лаб.",
                description = null,
                isImportant = Importance.Normal,
            ),
        LessonTypes.Other to
            createLessonType(
                title = "Другое",
                shortTitle = "Другое",
                description = null,
                isImportant = Importance.Normal,
            ),
    )

private fun createLessonType(
    title: String,
    shortTitle: String,
    description: String?,
    isImportant: Importance,
): LessonTypeConverter.LessonTypeCache {
    return LessonTypeConverter.LessonTypeCache(
        title = title,
        shortTitle = shortTitle,
        description = description,
        isImportant = isImportant,
    )
}
