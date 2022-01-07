package com.mospolytech.domain.schedule.model.place

import com.mospolytech.domain.base.model.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Place : Comparable<Place> {
    abstract val id: String
    abstract val title: String
    abstract val description: String
    abstract val imageUrl: String

    override fun compareTo(other: Place): Int {
        return when {
            this is Offline && other is Offline -> this.title.compareTo(other.title)
            this is Online && other is Online -> this.title.compareTo(other.title)
            this is Offline -> -1
            else -> 1
        }
    }

    @SerialName("offline")
    data class Offline(
        override val id: String,
        override val title: String,
        override val description: String,
        override val imageUrl: String,
        val location: Location
    ) : Place()

    @SerialName("online")
    data class Online(
        override val id: String,
        override val title: String,
        override val description: String,
        override val imageUrl: String,
        val url: String
    ) : Place()
}