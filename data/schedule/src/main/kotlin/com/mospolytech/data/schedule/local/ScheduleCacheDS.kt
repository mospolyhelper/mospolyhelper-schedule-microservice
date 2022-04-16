package com.mospolytech.data.schedule.local

import com.mospolytech.data.schedule.converters.mergeLessons
import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ScheduleCacheDS {
    var scheduleCache: List<LessonDateTimes> = emptyList()
    var scheduleCacheUpdateDateTime: LocalDateTime = LocalDateTime.MIN
}