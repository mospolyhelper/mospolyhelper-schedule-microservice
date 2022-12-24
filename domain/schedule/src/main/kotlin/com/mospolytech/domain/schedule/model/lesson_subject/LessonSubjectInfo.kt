package com.mospolytech.domain.schedule.model.lesson_subject

import kotlinx.serialization.Serializable

@Serializable
data class LessonSubjectInfo(
    val id: String,
    val title: String,
    val type: String?,
) : Comparable<LessonSubjectInfo> {
    override fun compareTo(other: LessonSubjectInfo): Int {
        return title.compareTo(other.title)
    }
}

val LessonSubjectInfo.description
    get() = buildString {
        type?.let {
            append(type)
        }
    }
