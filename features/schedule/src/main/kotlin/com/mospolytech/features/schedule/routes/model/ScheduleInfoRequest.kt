package com.mospolytech.features.schedule.routes.model

import com.mospolytech.domain.schedule.model.scheduleInfo.ScheduleInfoObject
import io.ktor.resources.*

@Resource("/{type}/{key}")
data class ScheduleInfoRequest(
    val type: ScheduleInfoObject,
    val key: String,
)
