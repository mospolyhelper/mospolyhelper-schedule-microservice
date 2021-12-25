package com.mospolytech.domain.schedule.utils

import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import java.time.LocalDateTime

fun List<LessonDateTimes>.filterByGroup(groupId: String): List<LessonDateTimes> {
    return this.filter { it.lesson.groups.any { it.title == groupId } }
}

fun List<LessonDateTimes>.filterByPlace(placeId: String): List<LessonDateTimes> {
    return this.filter { it.lesson.places.any { it.id == placeId } }
}

fun List<LessonDateTimes>.filterByTeacher(teacherId: String): List<LessonDateTimes> {
    return this.filter { it.lesson.teachers.any { it.id == teacherId } }
}

fun List<LessonDateTimes>.filterByPlaces(placeIds: List<String>): List<LessonDateTimes> {
    return this.filter { it.lesson.places.any { it.id in placeIds } }
}
