package com.mospolytech.features.schedule.routes.model

import com.mospolytech.domain.schedule.model.source.ScheduleSources
import io.ktor.server.locations.*

@Location("/{type}/{key}")
data class ScheduleRequest(
    val type: ScheduleSources,
    val key: String
)