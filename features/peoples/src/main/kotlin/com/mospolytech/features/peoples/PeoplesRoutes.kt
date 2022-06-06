package com.mospolytech.features.peoples

import com.mospolytech.domain.peoples.repository.PeoplesRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.utils.getTokenOrRespondError
import com.mospolytech.features.base.utils.respondResult
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.peoplesRoutesV1(repository: PeoplesRepository) {
    routing {
        authenticate(AuthConfigs.Mpu, optional = true) {
            route("/peoples") {
                route("/students") {
                    get<NameRequest> {
                        call.respond(repository.getStudents(it.name, it.page, it.pageSize))
                    }
                    get<NoNameRequest> {
                        call.respond(repository.getStudents(it.name, it.page, it.pageSize))
                    }
                    get<Empty> {
                        call.respond(repository.getStudents())
                    }
                    get {
                        call.respond(repository.getStudents())
                    }
                }
                route("/teachers") {
                    get<NameRequest> {
                        call.respond(repository.getTeachers(it.name, it.page, it.pageSize))
                    }
                    get<NoNameRequest> {
                        call.respond(repository.getTeachers(it.name, it.page, it.pageSize))
                    }
                    get<Empty> {
                        call.respond(repository.getTeachers())
                    }
                    get {
                        call.respond(repository.getTeachers())
                    }
                }
                route("/classmates") {
                    get {
                        val token = call.getTokenOrRespondError() ?: return@get
                        call.respond(repository.getClassmates(token))
                    }
                }
                route("/update") {
                    get {
                        repository.updateData()
                        call.respond("updated")
                    }
                }
            }
        }
    }
}

@Location("/{pageSize}/{page}/{name}")
data class NameRequest(
    val name: String = "",
    val page: Int = 1,
    val pageSize: Int = 100
)

@Location("/{pageSize}/{page}/")
data class NoNameRequest(
    val name: String = "",
    val page: Int = 1,
    val pageSize: Int = 100
)
@Location("/")
object Empty