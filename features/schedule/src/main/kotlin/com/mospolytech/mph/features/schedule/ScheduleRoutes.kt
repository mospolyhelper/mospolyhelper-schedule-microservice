package com.mospolytech.mph.features.schedule

import com.mospolytech.mph.data.schedule.ScheduleService
import com.mospolytech.mph.domain.schedule.model.ScheduleSources
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.scheduleRoutes() {
    routing {
        get("/schedule-sources") {
            call.respond(ScheduleSources.values())
        }
        get("/schedule") {
            val schedule = ScheduleService(HttpClient())
                .getScheduleByGroup("181-721", false)
            call.respondText(schedule, ContentType.parse("application/json"))
        }
    }
}