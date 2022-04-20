package com.mospolytech.features.schedule.routes

import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.repository.FreePlacesRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.freePlaceRoutesV1(repository: FreePlacesRepository) {
    route("/schedule") {
        route("/places") {
            post("/free") {
                val filters = call.receive<PlaceFilters>()
                call.respond(repository.getPlaces(filters))
            }
            get("/occupancy/{placeId}") {
                val placeId = call.parameters["placeId"]!!
                call.respond(repository.getPlaceOccupancy(placeId))
            }
        }
    }
}