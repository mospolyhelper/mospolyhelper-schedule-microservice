package com.mospolytech.features.schedule.routes.model

import com.mospolytech.domain.schedule.model.source.ScheduleSources
import io.ktor.server.locations.*

@Location("/{type}")
data class ScheduleSourceListRequest(
    val type: ScheduleSources
)
