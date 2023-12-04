package com.mospolytech.features.peoples

import com.mospolytech.domain.base.AppConfig
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.repository.StudentsRepository
import com.mospolytech.domain.peoples.repository.TeachersRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.utils.getTokenOrRespondError
import com.mospolytech.features.base.utils.respondResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.peoplesRoutesV1(
    studentsRepository: StudentsRepository,
    teachersRepository: TeachersRepository,
    studentsJobLauncher: StudentsJobLauncher,
    teachersJobLauncher: TeachersJobLauncher,
    appConfig: AppConfig,
) {
    routing {
        authenticate(AuthConfigs.MPU, optional = true) {
            route("/peoples") {
                route("/students") {
                    get {
                        val query = call.request.queryParameters["query"] ?: ""
                        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 100

                        call.getTokenOrRespondError() ?: return@get
                        call.respond(
                            studentsRepository.getShortStudents(
                                query = query,
                                page = page,
                                limit = limit,
                            ),
                        )
                    }
                }
                route("/teachers") {
                    get {
                        val query = call.request.queryParameters["query"] ?: ""
                        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 100

                        call.getTokenOrRespondError() ?: return@get
                        call.respond(
                            teachersRepository.getTeachers(
                                query = query,
                                page = page,
                                limit = limit,
                            ),
                        )
                    }
                }
                route("/classmates") {
                    get {
                        val token = call.getTokenOrRespondError() ?: return@get
                        val result =
                            studentsRepository.getClassmates(token)
                                .map {
                                    PagingDTO.from(it)
                                }
                        call.respondResult(result)
                    }
                }
                route("/update-students") {
                    get {
                        if (call.request.queryParameters["key"] != appConfig.adminKey) {
                            call.respond(HttpStatusCode.Forbidden, "")
                            return@get
                        }
                        val recreateDb = call.request.queryParameters["recreate"] == "1"
                        if (recreateDb) {
                            studentsRepository.updateData(recreateDb = true)
                        } else {
                            studentsJobLauncher.launchNow()
                        }
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
                        if (recreateDb) {
                            teachersRepository.updateData(recreateDb = true)
                        } else {
                            teachersJobLauncher.launchNow()
                        }
                        call.respond("updated")
                    }
                }
            }
        }
    }
}
