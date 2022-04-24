package com.mospolytech.domain.schedule.model.lesson_subject

import kotlinx.serialization.Serializable

@Serializable
data class LessonSubject(
    val id: String,
    val title: String
) : Comparable<LessonSubject> {
    override fun compareTo(other: LessonSubject): Int {
        return title.compareTo(other.title)
    }

    companion object {
        private val map = mutableMapOf<LessonSubjectInfo, LessonSubject>()

        fun from(info: LessonSubjectInfo) =
            map.getOrPut(info) {
                LessonSubject(
                    id = info.id,
                    title = info.title
                )
            }
    }
}