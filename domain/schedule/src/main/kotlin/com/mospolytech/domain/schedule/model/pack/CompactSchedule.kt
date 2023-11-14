package com.mospolytech.domain.schedule.model.pack

import com.mospolytech.domain.schedule.model.lessonSubject.CompactLessonSubjectInfo
import com.mospolytech.domain.schedule.model.place.CompactPlaceInfo
import kotlinx.serialization.Serializable

@Serializable
data class CompactSchedule(
    val lessons: List<CompactLessonEvent>,
    val subjects: List<CompactLessonSubjectInfo>,
    val attendees: List<AttendeeInfo>,
    val places: List<CompactPlaceInfo>,
) {
    companion object {
        val empty by lazy {
            CompactSchedule(
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
            )
        }
    }
}
