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

val Teacher.description: String?
    get() {
        return buildString {
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
    }
