package com.mospolytech.features.schedule.routes

import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.lessonsRoutesV1(
    repository: LessonsRepository,
    scheduleRepository: ScheduleRepository
) {
    route("/lessons") {
        get {
            call.respond(repository.getLessons())
        }
    }
}