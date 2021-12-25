package com.mospolytech.features.schedule.routes

import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.features.schedule.ScheduleRequest
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.scheduleRoutesV1(repository: ScheduleRepository) {
    route("/schedules") {
        get {
            call.respond(repository.getSchedule())
        }
        get("/complex") {
            call.respond(mapOf("Cant" to "do this"))
        }
        get<ScheduleRequest> {
            call.respond(repository.getSchedule(ScheduleSource(it.type, it.key)))
        }
    }
}