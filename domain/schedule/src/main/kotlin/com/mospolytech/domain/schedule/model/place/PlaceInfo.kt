package com.mospolytech.domain.schedule.model.place

import com.mospolytech.domain.base.model.Location
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import io.ktor.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PlaceInfo {
    abstract val id: String
    abstract val title: String

    companion object {
        val map = mutableMapOf<String, PlaceInfo>()
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
        val description: Map<String, String>? = null
    ) : PlaceInfo() {
        companion object {
            fun create(
                title: String,
                areaAlias: String? = null,
                street: String? = null,
                building: String? = null,
                floor: String? = null,
                auditorium: String? = null,
                location: Location? = null,
                description: Map<String, String>? = null
            ) = Building(
                id = (Building::class.simpleName + title),
                title = title,
                areaAlias,
                street,
                building,
                floor,
                auditorium,
                location,
                description
            ).run {
                map.getOrPut(id) { this }
            }
        }
    }

    @Serializable
    @SerialName("online")
    data class Online(
        override val id: String,
        override val title: String,
        val url: String? = null,
        val description: Map<String, String>? = null
    ) : PlaceInfo() {
        companion object {
            fun create(
                title: String,
                url: String? = null,
                description: Map<String, String>? = null
            ) = Online(
                id = (Online::class.simpleName + title + url),
                title = title,
                url = url,
                description = description,
            ).run {
                map.getOrPut(id) { this }
            }
        }
    }

    @Serializable
    @SerialName("other")
    data class Other(
        override val id: String,
        override val title: String,
        val description: Map<String, String>? = null
    ) : PlaceInfo() {
        companion object {
            fun create(
                title: String,
                description: Map<String, String>? = null
            ) = Other(
                id = (Other::class.simpleName + title),
                title = title,
                description = description,
            ).run {
                map.getOrPut(id) { this }
            }
        }
    }

    @Serializable
    @SerialName("unclassified")
    data class Unclassified(
        override val id: String,
        override val title: String,
        val description: Map<String, String>? = null
    ) : PlaceInfo()  {
        companion object {
            fun create(
                title: String,
                description: Map<String, String>? = null
            ) = Unclassified(
                id = (Unclassified::class.simpleName + title),
                title = title,
                description = description,
            ).run {
                map.getOrPut(id) { this }
            }
        }
    }
}