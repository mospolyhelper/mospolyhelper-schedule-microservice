package com.mospolytech.features.peoples

import com.mospolytech.domain.base.AppConfig
import com.mospolytech.domain.peoples.repository.StudentsRepository
import com.mospolytech.domain.peoples.repository.TeachersRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.utils.getTokenOrRespondError
import com.mospolytech.features.base.utils.respondResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.peoplesRoutesV1(
    studentsRepository: StudentsRepository,
    teachersRepository: TeachersRepository,
    appConfig: AppConfig,
) {
    routing {
        authenticate(AuthConfigs.Mpu, optional = true) {
            route("/peoples") {
                route("/students") {
                    get<NameRequest> {
                        call.getTokenOrRespondError() ?: return@get
                        call.respond(studentsRepository.getStudents(it.name, it.page, it.pageSize))
                    }
                    get<NoNameRequest> {
                        call.getTokenOrRespondError() ?: return@get
                        call.respond(studentsRepository.getStudents(it.name, it.page, it.pageSize))
                    }
                    get<Empty> {
                        call.getTokenOrRespondError() ?: return@get
                        call.respond(studentsRepository.getStudents())
                    }
                    get {
                        call.getTokenOrRespondError() ?: return@get
                        call.respond(studentsRepository.getStudents())
                    }
                }
                route("/teachers") {
                    get<NameRequest> {
                        call.getTokenOrRespondError() ?: return@get
                        call.respond(teachersRepository.getTeachers(it.name, it.page, it.pageSize))
                    }
                    get<NoNameRequest> {
                        call.getTokenOrRespondError() ?: return@get
                        call.respond(teachersRepository.getTeachers(it.name, it.page, it.pageSize))
                    }
                    get<Empty> {
                        call.getTokenOrRespondError() ?: return@get
                        call.respond(teachersRepository.getTeachers())
                    }
                    get {
                        call.getTokenOrRespondError() ?: return@get
                        call.respond(teachersRepository.getTeachers())
                    }
                }
                route("/classmates") {
                    get {
                        val token = call.getTokenOrRespondError() ?: return@get
                        call.respondResult(studentsRepository.getClassmates(token))
                    }
                    get<Empty> {
                        val token = call.getTokenOrRespondError() ?: return@get
                        call.respondResult(studentsRepository.getClassmates(token))
                    }
                }
                route("/update-students") {
                    get {
                        if (call.request.queryParameters["key"] != appConfig.adminKey) {
                            call.respond(HttpStatusCode.Forbidden, "")
                            return@get
                        }
                        val recreateDb = call.request.queryParameters["recreate"] == "1"
                        studentsRepository.updateData(recreateDb)
                        call.respond("updated")
                    }
                }
                route("/update-teachers") {
                    get {
                        if (call.request.queryParameters["key"] != appConfig.adminKey) {
                            call.respond(HttpStatusCode.Forbidden, "")
                            return@get
                        }
                        val recreateDb = call.request.queryParameters["recreate"] == "1"
                        teachersRepository.updateData(recreateDb)
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
    val pageSize: Int = 100,
)

@Location("/{pageSize}/{page}/")
data class NoNameRequest(
    val name: String = "",
    val page: Int = 1,
    val pageSize: Int = 100,
)

@Location("/")
object Empty
