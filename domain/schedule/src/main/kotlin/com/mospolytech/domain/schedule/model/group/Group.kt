package com.mospolytech.domain.schedule.model.group

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: String,
    val title: String
) : Comparable<Group> {
    override fun compareTo(other: Group): Int {
        return title.compareTo(other.title)
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