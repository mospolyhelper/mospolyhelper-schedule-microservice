package com.mospolytech.domain.schedule.model.place

import com.mospolytech.domain.base.model.Location
import com.mospolytech.domain.base.utils.ifNotEmpty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PlaceInfo : Comparable<PlaceInfo> {
    abstract val id: String
    abstract val title: String

    private fun getTypeNumber(): Int {
        return when (this) {
            is Building -> 0
            is Online -> 1
            is Other -> 2
            is Unclassified -> 3
        }
    }

    override fun compareTo(other: PlaceInfo): Int {
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
        val areaAlias: String? = null,
        val street: String? = null,
        val building: String? = null,
        val floor: String? = null,
        val auditorium: String? = null,
        val location: Location? = null,
        val description: String? = null
    ) : PlaceInfo()

    @Serializable
    @SerialName("online")
    data class Online(
        override val id: String,
        override val title: String,
        val url: String? = null,
        val description: String? = null
    ) : PlaceInfo()

    @Serializable
    @SerialName("other")
    data class Other(
        override val id: String,
        override val title: String,
        val description: String? = null
    ) : PlaceInfo()

    @Serializable
    @SerialName("unclassified")
    data class Unclassified(
        override val id: String,
        override val title: String,
        val description: String? = null
    ) : PlaceInfo()
}

val PlaceInfo.description: String
    get() {
        return when (this@description) {
            is PlaceInfo.Building -> buildString {
                street?.let {
                    append(street)
                }

                building?.let {
                    ifNotEmpty { append(", ") }
                    append("$building-й корус")
                }

                floor?.let {
                    ifNotEmpty { append(", ") }
                    append("$floor-й этаж")
                }
            }
            is PlaceInfo.Online -> buildString {
                url?.let {
                    append(url)
                }
            }
            is PlaceInfo.Other -> buildString {
                description?.let {
                    append(description)
                }
            }
            is PlaceInfo.Unclassified -> buildString {
                description?.let {
                    append(description)
                }
            }
        }
    }
