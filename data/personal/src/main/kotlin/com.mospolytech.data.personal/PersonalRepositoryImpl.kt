package com.mospolytech.data.personal

import com.mospolytech.domain.personal.model.Personal
import com.mospolytech.domain.personal.repository.PersonalRepository

class PersonalRepositoryImpl(
    private val service: PersonalService
): PersonalRepository {
    override suspend fun getPersonalInfo(token: String): Result<Personal> {
        return runCatching {
            val personalResponse = service.getPersonalInfo(token)
            personalResponse.toModel()
        }
    }
}