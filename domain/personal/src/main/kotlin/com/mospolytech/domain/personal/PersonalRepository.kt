package com.mospolytech.domain.personal

interface PersonalRepository {
    suspend fun getPersonalInfo(token: String): Result<Personal>

    suspend fun getPersonalGroup(token: String): Result<String>

    suspend fun getCourse(token: String): Result<Int>
}
