package com.mospolytech.domain.schedule.model.lesson

import com.mospolytech.domain.schedule.model.group.Group
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubject
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonType
import com.mospolytech.domain.schedule.model.place.Place
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.teacher.Teacher
import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val type: LessonType,
    val subject: LessonSubject,
    val teachers: List<Teacher>,
    val groups: List<Group>,
    val places: List<Place>,
): Comparable<Lesson> {
    override fun compareTo(other: Lesson): Int {
        val comparing = subject.title.compareTo(other.subject.title)
        return if (comparing != 0) {
            comparing
        } else {
            type.title.compareTo(other.type.title)
        }
    }
}