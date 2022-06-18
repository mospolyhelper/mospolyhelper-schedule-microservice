package com.mospolytech.domain.peoples.model

import com.mospolytech.domain.base.model.Department
import com.mospolytech.domain.base.utils.ifNotEmpty
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val id: String,
    val name: String,
    val avatar: String?,
    val stuffType: String?,
    val grade: String?,
    val departmentParent: Department?,
    val department: Department?,
    val email: String?,
    val sex: String?,
    val birthday: LocalDate?
) : Comparable<Teacher> {
    override fun compareTo(other: Teacher): Int {
        return name.compareTo(other.name)
    }
}

val Teacher.description: String
    get() {
        return buildString {
            grade?.let { append(it) }

            department?.let {
                ifNotEmpty { append(", ") }
                append(department.title)
            }

            departmentParent?.let {
                ifNotEmpty { append(", ") }
                append(departmentParent.title)
            }
        }
    }
