package com.mospolytech.features.schedule.routes

import com.mospolytech.domain.personal.repository.PersonalRepository
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSources
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.features.base.*
import com.mospolytech.features.schedule.routes.model.ScheduleComplexRequest
import com.mospolytech.features.schedule.routes.model.ScheduleRequest
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.scheduleRoutesV1(
    repository: ScheduleRepository,
    userRepository: PersonalRepository
) {
    route("/schedules") {
        get {
            call.respond(repository.getSchedule())
        }
        post("/complex") {
            call.receive<ScheduleComplexRequest>()
            call.respond(repository.getSchedule())
        }
        get<ScheduleRequest> {
            call.respond(repository.getSchedule(ScheduleSource(it.type, it.key)))
        }
        route("/pack") {
            get {
                call.respond(repository.getSchedulePack())
            }
            get<ScheduleRequest> {
                call.respond(repository.getSchedulePack(ScheduleSource(it.type, it.key)))
            }
        }
        authenticate(AuthConfigs.Mpu, optional = true) {
            get("my") {
                val token = call.getTokenOrRespondError() ?: return@get

                call.respondResult(
                    userRepository.getPersonalInfo(token).map {
                        repository.getSchedulePack(ScheduleSource(ScheduleSources.Group, it.group))
                    }
                )
            }
        }
    }
}