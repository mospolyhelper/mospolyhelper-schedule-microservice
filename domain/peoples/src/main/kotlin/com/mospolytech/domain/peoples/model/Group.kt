package com.mospolytech.domain.peoples.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: String,
    val title: String,
    val course: Int?,
    val faculty: String?,
    val direction: String?,
) : Comparable<Group> {
    override fun compareTo(other: Group): Int {
        return this.title.compareTo(other.title)
    }
}
