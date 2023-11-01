package com.mospolytech.domain.schedule.model.teacher

import kotlinx.serialization.Serializable

@Serializable
data class CompactLessonTeacherInfo(
    val id: String,
    val name: String,
    val description: String,
    val avatar: String?,
) : Comparable<CompactLessonTeacherInfo> {
    override fun compareTo(other: CompactLessonTeacherInfo): Int {
        return name.compareTo(other.name)
    }
}
