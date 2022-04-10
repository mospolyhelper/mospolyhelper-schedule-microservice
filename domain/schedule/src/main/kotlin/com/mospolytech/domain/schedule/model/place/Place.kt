package com.mospolytech.domain.schedule.model.place

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val id: String,
    val title: String
) : Comparable<Place> {
    override fun compareTo(other: Place): Int {
        return title.compareTo(other.title)
    }

    companion object {
        private val map = mutableMapOf<PlaceInfo, Place>()

        fun from(info: PlaceInfo) =
            map.getOrPut(info) {
                Place(
                    id = info.id,
                    title = info.title
                )
            }
    }
}