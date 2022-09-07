package com.mospolytech.domain.schedule.model.teacher

import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val id: String,
    val name: String
) : Comparable<Teacher> {
    override fun compareTo(other: Teacher): Int {
        return name.compareTo(other.name)
    }

    companion object {
        private val map = mutableMapOf<Teacher, Teacher>()

        fun from(info: Teacher) =
            map.getOrPut(info) {
                Teacher(
                    id = info.id,
                    name = info.name
                )
            }
    }
}
