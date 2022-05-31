package com.mospolytech.features.peoples

import com.mospolytech.domain.peoples.repository.PeoplesRepository
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.peoplesRoutesV1(repository: PeoplesRepository) {
    routing {
        route("/peoples") {
            get {
                call.respond(repository.getTeachers())
            }
            route("/students") {
                get<NameDtoRequest> {
                    call.respond(repository.getStudents(it.name, it.page, it.count))
                }
                get<DtoRequest> {
                    call.respond(repository.getStudents("", it.page, it.count))
                }
                get {
                    call.respond(repository.getStudents())
                }
            }
            route("/teachers") {
                get<NameDtoRequest> {
                    call.respond(repository.getTeachers(it.name, it.page, it.count))
                }
                get<DtoRequest> {
                    call.respond(repository.getTeachers("", it.page, it.count))
                }
                get {
                    call.respond(repository.getTeachers())
                }
            }
            route("/classmates") {
                get<NameRequest> {
                    call.respond(repository.getClassmates(it.name))
                }
                get {
                    call.respond(repository.getClassmates())
                }
            }
        }
    }
}

@Location("/{name}")
data class NameDtoRequest(
    val name: String = "",
    val page: Int = 1,
    val count: Int = 100
)

@Location("/{name}")
data class NameRequest(
    val name: String = ""
)

@Location("/")
data class DtoRequest(
    val page: Int = 1,
    val count: Int = 100
)