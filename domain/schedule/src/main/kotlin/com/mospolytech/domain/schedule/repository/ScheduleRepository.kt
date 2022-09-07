package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceFull
import com.mospolytech.domain.schedule.model.source.ScheduleSources

interface ScheduleRepository {
    suspend fun getCompactSchedule(): CompactSchedule
    suspend fun getCompactSchedule(filter: ScheduleComplexFilter): CompactSchedule
    suspend fun getCompactSchedule(source: ScheduleSource): CompactSchedule
    suspend fun findGroupByTitle(title: String): String?

    suspend fun getSourceList(sourceType: ScheduleSources): List<ScheduleSourceFull>
    suspend fun updateData(recreateDb: Boolean)
}
