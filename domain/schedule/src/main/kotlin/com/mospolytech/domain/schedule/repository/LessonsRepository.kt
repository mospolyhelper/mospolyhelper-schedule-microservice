package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes

interface LessonsRepository {
    suspend fun getLessons(): List<LessonDateTimes>
}