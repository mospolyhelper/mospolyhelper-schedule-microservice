package com.mospolytech.domain.peoples.model

import com.mospolytech.domain.base.utils.ifNotEmpty
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
    get() =
        buildString {
            group?.let {
                append(it.title)
            }

            course?.let {
                ifNotEmpty { append(", ") }
                append("$course-й курс")
            }

            group?.let {
                group.direction?.let {
                    ifNotEmpty { append(", ") }
                    append(group.direction)
                }
            }
        }
