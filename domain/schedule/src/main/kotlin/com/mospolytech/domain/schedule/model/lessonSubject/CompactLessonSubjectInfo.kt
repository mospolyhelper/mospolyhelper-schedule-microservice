package com.mospolytech.domain.schedule.model.lessonSubject

import kotlinx.serialization.Serializable

@Serializable
data class CompactLessonSubjectInfo(
    val id: String,
    val title: String,
    val type: String?,
    val description: String,
) : Comparable<CompactLessonSubjectInfo> {
    override fun compareTo(other: CompactLessonSubjectInfo): Int {
        return title.compareTo(other.title)
    }
}
