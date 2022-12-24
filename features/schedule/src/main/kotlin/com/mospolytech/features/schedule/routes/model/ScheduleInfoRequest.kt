package com.mospolytech.features.schedule.routes.model

import com.mospolytech.domain.schedule.model.schedule_info.ScheduleInfoObject
import io.ktor.server.locations.*

@Location("/{type}/{key}")
data class ScheduleInfoRequest(
    val type: ScheduleInfoObject,
    val key: String,
)
