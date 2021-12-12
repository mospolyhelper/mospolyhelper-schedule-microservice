package com.mospolytech.domain.schedule.utils

import com.mospolytech.domain.schedule.model.LessonDateTimes
import com.mospolytech.domain.schedule.model.ScheduleSource

fun List<LessonDateTimes>.filterByGroup(group: String): List<LessonDateTimes> {
    return this.filter { it.lesson.groups.any { it.title == group } }
}