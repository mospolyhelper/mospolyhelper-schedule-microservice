package com.mospolytech.domain.schedule.model.lessonSubject

import kotlinx.serialization.Serializable

@Serializable
data class LessonSubjectInfo(
    val id: String,
    val title: String,
    val type: String?,
    val description: String?,
) : Comparable<LessonSubjectInfo> {
    override fun compareTo(other: LessonSubjectInfo): Int {
        return title.compareTo(other.title)
    }
}
