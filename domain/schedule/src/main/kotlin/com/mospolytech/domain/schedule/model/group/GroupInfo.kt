package com.mospolytech.domain.schedule.model.group

import io.ktor.util.*
import kotlinx.serialization.Serializable

@Serializable
data class GroupInfo(
    val id: String,
    val title: String,
    val description: String
) {
    companion object {
        val map = mutableMapOf<String, GroupInfo>()

        fun create(
            title: String,
            description: String
        ) = GroupInfo(
            id = title,
            title = title,
            description = description
        ).run {
            map.getOrPut(id) { this }
        }
    }
}