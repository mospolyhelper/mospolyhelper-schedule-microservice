package com.mospolytech.domain.peoples.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentShort(
    val id: String,
    val name: String,
    val avatar: String?,
    val group: Group?,
    val course: Int?,
)

val StudentShort.description
    get() = buildString {
        group?.let {
            append(it.title)
        }

        course?.let {
            ifEmpty { append(", ") }
            append("$course-й курс")
        }

        group?.let {
            group.direction?.let {
                ifEmpty { append(", ") }
                append(group.direction)
            }
        }
    }
