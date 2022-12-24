package com.mospolytech.features.schedule.routes

import com.mospolytech.domain.base.AppConfig
import com.mospolytech.domain.personal.repository.PersonalRepository
import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSources
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.utils.getTokenOrRespondError
import com.mospolytech.features.base.utils.respondResult
import com.mospolytech.features.schedule.routes.model.ScheduleRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.scheduleRoutesV1(
    repository: ScheduleRepository,
    userRepository: PersonalRepository,
    appConfig: AppConfig,
) {
    route("/schedules") {
        route("/compact") {
            get {
                call.respond(repository.getCompactSchedule())
            }
            get<ScheduleRequest> {
                call.respond(repository.getCompactSchedule(ScheduleSource(it.type, it.key)))
            }
            post("/complex") {
                val filter = call.receive<ScheduleComplexFilter>()
                call.respond(repository.getCompactSchedule(filter))
            }
        }
        authenticate(AuthConfigs.Mpu, optional = true) {
            get("my") {
                val token = call.getTokenOrRespondError() ?: return@get

                call.respondResult(
                    userRepository.getPersonalInfo(token).map {
                        val id = repository.findGroupByTitle(it.group)
                        id?.let {
                            repository.getCompactSchedule(ScheduleSource(ScheduleSources.Group, id))
                        }
                    },
                )
            }
        }
        route("/update-schedules") {
            get {
                if (call.request.queryParameters["key"] != appConfig.adminKey) {
                    call.respond(HttpStatusCode.Forbidden, "")
                    return@get
                }
                val recreateDb = call.request.queryParameters["recreate"] == "1"
                repository.updateData(recreateDb)
                call.respond("updated")
            }
        }
    }
}
