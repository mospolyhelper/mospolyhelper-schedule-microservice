package com.mospolytech.features.schedule.routes

import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.features.schedule.routes.model.ScheduleSourceListRequest
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get

fun Routing.sourcesRoutesV1(repository: ScheduleRepository) {
    route("/schedule") {
        route("/sources") {
            get {
                call.respond(repository.getSourceTypes())
            }
            get<ScheduleSourceListRequest> {
                val query = call.request.queryParameters["query"] ?: ""
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 100

                val sourceList =
                    repository.getSourceList(
                        sourceType = it.type,
                        query = query,
                        page = page,
                        limit = limit,
                    )

                call.respond(sourceList)
            }
        }
    }
}
