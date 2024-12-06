package com.mospolytech.features.schedule.routes.model

import com.mospolytech.domain.schedule.model.source.ScheduleSourceTypes
import io.ktor.resources.*

@Resource("/{type}")
data class ScheduleSourceListRequest(
    val type: ScheduleSourceTypes,
)
