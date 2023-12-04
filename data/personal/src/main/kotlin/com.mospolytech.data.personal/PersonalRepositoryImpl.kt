package com.mospolytech.data.personal

import com.mospolytech.domain.services.personal.Personal
import com.mospolytech.domain.services.personal.PersonalRepository

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
}
