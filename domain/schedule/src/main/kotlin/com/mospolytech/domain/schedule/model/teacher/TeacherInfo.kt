package com.mospolytech.domain.schedule.model.teacher

import io.ktor.util.*
import kotlinx.serialization.Serializable

@Serializable
data class TeacherInfo(
    val id: String,
    val name: String,
    val description: String
) {
    companion object {
        val map = mutableMapOf<String, TeacherInfo>()

        fun create(
            name: String,
            description: String
        ) = TeacherInfo(
            id = name,
            name = name,
            description = description
        ).run {
            map.getOrPut(id) { this }
        }
    }
}