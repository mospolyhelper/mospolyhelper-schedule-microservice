package com.mospolytech.mph.features.schedule

import com.mospolytech.mph.data.schedule.ScheduleService
import com.mospolytech.mph.domain.schedule.model.ScheduleSources
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.scheduleRoutesV1(service: ScheduleService) {
    routing {
        route("/schedule") {
            get("/sources") {
                call.respond(ScheduleSources.values().map { it.name.lowercase() })
            }
        }


        route("schedules") {
            get {
                call.respond(service.getSchedules(false))
            }
            get("/complex") {
                call.respond(mapOf("Cant" to "do this"))
            }
            get<ScheduleRequest> {
                val schedule = service
                    .getScheduleByGroup(it.key, false)
                call.respondText(schedule, ContentType.parse("application/json"))
            }
        }
    }
}

@Location("/{type}/{key}")
data class ScheduleRequest(
    val type: ScheduleSources,
    val key: String
)