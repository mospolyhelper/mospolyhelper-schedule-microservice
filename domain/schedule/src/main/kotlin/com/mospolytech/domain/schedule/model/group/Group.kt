package com.mospolytech.domain.schedule.model.group

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: String,
    val title: String
) : Comparable<Group> {
    override fun compareTo(other: Group): Int {
        return if (title.length == other.title.length)
            title.compareTo(other.title)
        else
            title.length - other.title.length
    }

    companion object {
        private val map = mutableMapOf<GroupInfo, Group>()

        fun from(info: GroupInfo) =
            map.getOrPut(info) {
                Group(
                    id = info.id,
                    title = info.title
                )
            }
    }
}