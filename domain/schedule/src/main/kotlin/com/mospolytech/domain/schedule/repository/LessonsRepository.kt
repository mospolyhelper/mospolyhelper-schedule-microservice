package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes

interface LessonsRepository {
    suspend fun getLessons(): List<CompactLessonAndTimes>
}