package com.mospolytech.domain.schedule.model.group

import io.ktor.util.*
import kotlinx.serialization.Serializable

@Serializable
data class GroupInfo(
    val id: String,
    val title: String,
    val description: String,
    val course: String,
    val isEvening: Boolean
) {
    companion object {
        val map = mutableMapOf<String, GroupInfo>()

        fun create(
            title: String,
            description: String,
            course: String,
            isEvening: Boolean
        ) = GroupInfo(
            id = title,
            title = title,
            description = description,
            course = course,
            isEvening = isEvening
        ).run {
            map.getOrPut(id) { this }
        }
    }
}