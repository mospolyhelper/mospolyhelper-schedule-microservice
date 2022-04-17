package com.mospolytech.data.schedule.local

import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import java.time.LocalDateTime

class ScheduleCacheDS {
    var scheduleCache: List<CompactLessonAndTimes> = emptyList()
    var scheduleCacheUpdateDateTime: LocalDateTime = LocalDateTime.MIN
}