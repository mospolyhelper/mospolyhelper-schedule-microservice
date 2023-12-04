package com.mospolytech.domain.peoples.model

import com.mospolytech.domain.base.utils.ifNotEmpty
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: String,
    val name: String,
    val avatar: String?,
    val faculty: String?,
    val direction: String?,
    val group: Group?,
    val specialization: String?,
    val educationType: String?,
    val educationForm: String?,
    val payment: String?,
    val course: Int?,
    val years: String?,
    val code: String?,
    val branch: String?,
)

fun Student.toPerson(): Person {
    val description =
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
        }.ifEmpty { null }

    return Person(
        id = id,
        name = name,
        description = description,
        avatar = avatar,
    )
}
