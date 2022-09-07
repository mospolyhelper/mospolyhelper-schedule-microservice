package com.mospolytech.domain.peoples.model

import com.mospolytech.domain.base.utils.ifNotEmpty

@kotlinx.serialization.Serializable
data class Group(
    val id: String,
    val title: String,
    val course: Int?,
    val faculty: StudentFaculty?,
    val direction: StudentDirection?,
) : Comparable<Group> {
    override fun compareTo(other: Group): Int {
        return this.title.compareTo(other.title)
    }
}

val Group.description
    get() = buildString {
        course?.let { append("$course-й курс") }

        direction?.let {
            ifNotEmpty { append(", ") }
            append(direction.title)
        }

        faculty?.let {
            ifNotEmpty { append(", ") }
            if (faculty.titleShort != null) {
                append(faculty.titleShort)
            } else {
                append(faculty.title)
            }
        }
    }
