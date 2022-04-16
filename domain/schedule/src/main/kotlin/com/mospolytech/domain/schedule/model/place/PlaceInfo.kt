package com.mospolytech.domain.schedule.model.place

import com.mospolytech.domain.base.model.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PlaceInfo {
    abstract val id: String
    abstract val title: String

    @Serializable
    @SerialName("building")
    data class Building(
        override val id: String,
        override val title: String,
        val areaAlias: String? = null,
        val street: String? = null,
        val building: String? = null,
        val floor: String? = null,
        val auditorium: String? = null,
        val location: Location? = null,
        val description: Map<String, String>? = null
    ) : PlaceInfo()

    @Serializable
    @SerialName("online")
    data class Online(
        override val id: String,
        override val title: String,
        val url: String? = null,
        val description: Map<String, String>? = null
    ) : PlaceInfo()

    @Serializable
    @SerialName("other")
    data class Other(
        override val id: String,
        override val title: String,
        val description: Map<String, String>? = null
    ) : PlaceInfo()

    @Serializable
    @SerialName("unclassified")
    data class Unclassified(
        override val id: String,
        override val title: String,
        val description: Map<String, String>? = null
    ) : PlaceInfo()
}