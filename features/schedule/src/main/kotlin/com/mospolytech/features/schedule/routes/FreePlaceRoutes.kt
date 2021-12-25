package com.mospolytech.features.schedule.routes

import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.freePlaceRoutesV1(repository: ScheduleRepository) {
    route("/schedule") {
        post("/free-place") {
            val filters = call.receive<PlaceFilters>()
            call.respond(repository.getPlaces(filters))
        }
    }
}