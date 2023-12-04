package com.mospolytech.domain.peoples.model

import com.mospolytech.domain.base.utils.ifNotEmpty
import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val id: String,
    val name: String,
    val avatar: String?,
    val stuffType: String?,
    val grade: String?,
    val departmentParent: String?,
    val department: String?,
    val email: String?,
) : Comparable<Teacher> {
    override fun compareTo(other: Teacher): Int {
        return name.compareTo(other.name)
    }
}

fun Teacher.toPerson(): Person {
    val description =
        buildString {
            grade?.let { append(it) }

            department?.let {
                ifNotEmpty { append(", ") }
                append(department)
            }

            departmentParent?.let {
                ifNotEmpty { append(", ") }
                append(departmentParent)
            }
        }.ifEmpty { null }

    return Person(
        id = id,
        name = name,
        description = description,
        avatar = avatar,
    )
}
