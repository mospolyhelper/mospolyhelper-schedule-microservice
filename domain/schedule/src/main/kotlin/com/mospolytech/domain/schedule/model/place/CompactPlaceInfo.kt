package com.mospolytech.domain.schedule.model.place

import com.mospolytech.domain.base.model.Coordinates
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class CompactPlaceInfo : Comparable<CompactPlaceInfo> {
    abstract val id: String
    abstract val title: String
    abstract val description: String?

    private fun getTypeNumber(): Int {
        return when (this) {
            is Building -> 0
            is Online -> 1
            is Other -> 2
        }
    }

    override fun compareTo(other: CompactPlaceInfo): Int {
        val compareTypes = getTypeNumber().compareTo(other.getTypeNumber())
        if (compareTypes != 0) {
            return compareTypes
        } else {
            return title.compareTo(other.title)
        }
    }

    @Serializable
    @SerialName("building")
    data class Building(
        override val id: String,
        override val title: String,
        override val description: String?,
        val coordinates: Coordinates? = null,
    ) : CompactPlaceInfo()

    @Serializable
    @SerialName("online")
    data class Online(
        override val id: String,
        override val title: String,
        override val description: String?,
        val url: String? = null,
    ) : CompactPlaceInfo()

    @Serializable
    @SerialName("other")
    data class Other(
        override val id: String,
        override val title: String,
        override val description: String?,
    ) : CompactPlaceInfo()
}
