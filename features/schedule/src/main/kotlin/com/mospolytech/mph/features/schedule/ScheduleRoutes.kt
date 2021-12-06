package com.mospolytech.mph.features.schedule

import com.mospolytech.mph.domain.schedule.model.ScheduleSource
import com.mospolytech.mph.domain.schedule.model.ScheduleSources
import com.mospolytech.mph.domain.schedule.repository.ScheduleRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.scheduleRoutesV1(repository: ScheduleRepository) {
    routing {
        route("/schedule") {
            get("/sources") {
                call.respond(ScheduleSources.values().map { it.name.lowercase() })
            }
        }


        route("schedules") {
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

        route("lessons") {
            get {
                call.respond(repository.getLessons())
            }
        }
    }
}

@Location("/{type}/{key}")
data class ScheduleRequest(
    val type: ScheduleSources,
    val key: String
)