package com.mospolytech.domain.services.personal

interface PersonalRepository {
    suspend fun getPersonalInfo(token: String): Result<Personal>

    suspend fun getPersonalGroup(token: String): Result<String>

    suspend fun getCourse(token: String): Result<Int>

    suspend fun getSupportedServices(token: String): Result<SupportedServices>
}
