package com.mospolytech.features.performance

import com.mospolytech.domain.perfomance.repository.PerformanceRepository
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get

fun Application.performanceRoutesV1(repository: PerformanceRepository) {
    routing {
        route("/performance") {
            get {
                call.respond(repository.getMarks())
            }
            route("/semesters") {
                get<SemesterRequest> {
                    call.respond(repository.getMarksBySemester(it.semester))
                }
                get {
                    call.respond(repository.getSemesters())
                }
            }
            route("/courses") {
                get<CourseRequest> {
                    call.respond(repository.getMarksByCourse(it.course))
                }
                get {
                    call.respond(repository.getCourses())
                }
            }
        }
    }
}

@Location("/{semester}")
data class SemesterRequest(
    val semester: Int
)
@Location("/{course}")
data class CourseRequest(
    val course: Int
)