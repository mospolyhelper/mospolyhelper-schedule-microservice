package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceType
import com.mospolytech.domain.schedule.model.source.ScheduleSourceTypes

interface ScheduleRepository {
    suspend fun getCompactSchedule(): CompactSchedule

    suspend fun getCompactSchedule(filter: ScheduleComplexFilter): CompactSchedule

    suspend fun getCompactSchedule(
        id: String,
        type: ScheduleSourceTypes,
    ): CompactSchedule

    suspend fun findGroupByTitle(title: String): String?

    suspend fun getSourceList(
        sourceType: ScheduleSourceTypes,
        query: String,
        page: Int,
        limit: Int,
    ): PagingDTO<ScheduleSource>

    fun getSourceTypes(): List<ScheduleSourceType> {
        return listOf(
            ScheduleSourceType(
                id = ScheduleSourceTypes.Group.name.lowercase(),
                title = "Группы",
            ),
            ScheduleSourceType(
                id = ScheduleSourceTypes.Teacher.name.lowercase(),
                title = "Преподаватели",
            ),
            ScheduleSourceType(
                id = ScheduleSourceTypes.Student.name.lowercase(),
                title = "Студенты",
            ),
            ScheduleSourceType(
                id = ScheduleSourceTypes.Place.name.lowercase(),
                title = "Места занятий",
            ),
            ScheduleSourceType(
                id = ScheduleSourceTypes.Subject.name.lowercase(),
                title = "Предметы",
            ),
        )
    }

    suspend fun updateData(recreateDb: Boolean)
}
