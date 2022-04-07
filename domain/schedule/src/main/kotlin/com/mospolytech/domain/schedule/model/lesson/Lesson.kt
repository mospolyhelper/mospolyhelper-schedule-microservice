package com.mospolytech.domain.schedule.model.lesson

import com.mospolytech.domain.schedule.model.group.Group
import com.mospolytech.domain.schedule.model.lesson_type.LessonType
import com.mospolytech.domain.schedule.model.place.Place
import com.mospolytech.domain.schedule.model.teacher.Teacher
import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val title: String,
    val type: LessonType,
    val teachers: List<Teacher>,
    val groups: List<Group>,
    val places: List<Place>,
): Comparable<Lesson> {
    override fun compareTo(other: Lesson): Int {
        val comparing = title.compareTo(other.title)
        return if (comparing != 0) {
            comparing
        } else {
            type.title.compareTo(other.type.title)
        }
    }

}