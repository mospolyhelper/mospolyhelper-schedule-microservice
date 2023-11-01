package com.mospolytech.domain.schedule.model.group

@kotlinx.serialization.Serializable
data class CompactLessonGroup(
    val id: String,
    val title: String,
    val description: String,
) : Comparable<CompactLessonGroup> {
    override fun compareTo(other: CompactLessonGroup): Int {
        return this.title.compareTo(other.title)
    }
}
