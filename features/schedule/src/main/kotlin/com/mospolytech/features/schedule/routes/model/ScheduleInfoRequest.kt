package com.mospolytech.features.schedule.routes.model

import com.mospolytech.domain.schedule.model.schedule_info.ScheduleInfoObject
import com.mospolytech.domain.schedule.model.source.ScheduleSources
import io.ktor.server.locations.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Location("/{type}/{key}")
data class ScheduleInfoRequest(
    val type: ScheduleInfoObject,
    val key: String
)