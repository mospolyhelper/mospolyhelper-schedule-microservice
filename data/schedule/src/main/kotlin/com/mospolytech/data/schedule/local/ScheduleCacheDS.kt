package com.mospolytech.data.schedule.local

import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import java.time.LocalDateTime

class ScheduleCacheDS {
    var scheduleCache: List<LessonDateTimes> = emptyList()
    var scheduleCacheUpdateDateTime: LocalDateTime = LocalDateTime.MIN
}