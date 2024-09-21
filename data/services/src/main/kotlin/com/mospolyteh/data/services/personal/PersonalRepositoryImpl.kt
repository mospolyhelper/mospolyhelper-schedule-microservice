package com.mospolyteh.data.services.personal

import com.mospolytech.domain.services.personal.Personal
import com.mospolytech.domain.services.personal.PersonalRepository
import com.mospolytech.domain.services.personal.SupportedServices

class PersonalRepositoryImpl(
    private val service: PersonalService,
) : PersonalRepository {
    override suspend fun getPersonalInfo(token: String): Result<Personal> {
        return runCatching {
            val personalResponse = service.getPersonalInfo(token)
            personalResponse.toModel()
        }
    }

    override suspend fun getPersonalGroup(token: String): Result<String> {
        return runCatching {
            val personalResponse = service.getPersonalInfo(token)
            personalResponse.user.group
        }
    }

    override suspend fun getCourse(token: String): Result<Int> {
        return runCatching {
            service.getPersonalInfo(token).user.course.toIntOrNull() ?: 1
        }
    }

    override suspend fun getSupportedServices(token: String): Result<SupportedServices> {
        return runCatching {
            val personalInfo = service.getPersonalInfo(token)
            val isStudent = personalInfo.user.isStudent()

            SupportedServices(
                services =
                buildList {
                    if (isStudent) {
                        add(SupportedServices.Services.PERFORMANCE)
                    }
                    add(SupportedServices.Services.PAYMENTS)
                    add(SupportedServices.Services.PEOPLE)
                },
                people =
                SupportedServices.PeopleService(
                    screens =
                    buildList {
                        add(
                            SupportedServices.PeopleService.PeopleScreen(
                                title = "Студенты",
                                icon = "https://img.icons8.com/fluency/196/student-male.png",
                                endpointName = "account-peoples-students",
                                queryHint = "Имя или группа",
                            ),
                        )

                        add(
                            SupportedServices.PeopleService.PeopleScreen(
                                title = "Преподаватели",
                                icon = "https://img.icons8.com/fluency/196/teacher.png",
                                endpointName = "account-peoples-teachers",
                                queryHint = "Имя",
                            ),
                        )

                        if (isStudent) {
                            add(
                                SupportedServices.PeopleService.PeopleScreen(
                                    title = "Одногруппники",
                                    icon = "https://img.icons8.com/fluency/196/conference-call.png",
                                    endpointName = "account-peoples-classmates",
                                    queryHint = "Имя",
                                ),
                            )
                        }
                    },
                ),
            )
        }
    }
}
