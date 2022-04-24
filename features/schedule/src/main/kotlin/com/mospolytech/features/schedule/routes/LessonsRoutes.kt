package com.mospolytech.features.schedule.routes

import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.features.schedule.routes.model.ScheduleRequest
import io.ktor.server.application.*
import io.ktor.server.locations.*
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
        route("/review") {
            post("/complex") {
                call.respond(repository.getLessons())
            }
            get<ScheduleRequest> {
                call.respond(scheduleRepository.getLessonsReview(ScheduleSource(it.type, it.key)))
            }
        }
    }
}