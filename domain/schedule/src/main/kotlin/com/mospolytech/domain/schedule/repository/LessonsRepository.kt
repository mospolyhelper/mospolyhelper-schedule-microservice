package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.pack.CompactLessonEvent
import com.mospolytech.domain.schedule.model.pack.CompactSchedule

interface LessonsRepository {
    suspend fun updateSchedule()

    suspend fun getAllLessons(): CompactSchedule

    suspend fun getLessonsByGroup(groupId: String): CompactSchedule

    suspend fun getLessonsByStudent(studentId: String): CompactSchedule

    suspend fun getLessonsByTeacher(teacherId: String): CompactSchedule

    suspend fun getLessonsByPlace(placeId: String): CompactSchedule

    suspend fun getLessonsBySubject(subjectId: String): CompactSchedule

    suspend fun getLessonsByFilter(filter: ScheduleComplexFilter): CompactSchedule

    suspend fun getLessonsByPlaces(placeIds: List<String> = emptyList()): List<CompactLessonEvent>
}
