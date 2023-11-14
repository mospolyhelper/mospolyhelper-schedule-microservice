package com.mospolytech.domain.peoples.model

@kotlinx.serialization.Serializable
data class GroupShort(
    val id: String,
    val title: String,
    val description: String?,
) : Comparable<GroupShort> {
    override fun compareTo(other: GroupShort): Int {
        return this.title.compareTo(other.title)
    }
}
