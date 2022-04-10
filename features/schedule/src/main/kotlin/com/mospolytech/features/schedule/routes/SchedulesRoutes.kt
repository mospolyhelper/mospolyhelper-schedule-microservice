package com.mospolytech.features.schedule.routes

import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.MpuPrincipal
import com.mospolytech.features.base.respondResult
import com.mospolytech.features.schedule.ScheduleRequest
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.scheduleRoutesV1(repository: ScheduleRepository) {
    route("/schedules") {
        get {
            call.respond(repository.getSchedule())
        }
        post("/complex") {
            call.respond(repository.getSchedule())
        }
        get<ScheduleRequest> {
            call.respond(repository.getSchedule(ScheduleSource(it.type, it.key)))
        }
        route("/pack") {
            get<ScheduleRequest> {
                call.respond(repository.getSchedulePack(ScheduleSource(it.type, it.key)))
            }
        }
        authenticate(AuthConfigs.Mpu, optional = true) {
            get("my") {
                val principal: MpuPrincipal? = call.authentication.principal()

            }
        }
    }
}