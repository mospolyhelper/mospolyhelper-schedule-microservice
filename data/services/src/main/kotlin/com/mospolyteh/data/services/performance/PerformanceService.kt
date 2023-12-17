package com.mospolyteh.data.services.performance

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class PerformanceService(
    private val client: HttpClient,
) {
    companion object {
        private const val BASE_URL = "https://e.mospolytech.ru"
        private const val API_URL = "$BASE_URL/old/lk_api.php"

        private const val GET_PERFOMANCE = "$API_URL?getAcademicPerformance="
    }

    suspend fun getPerformanceInfo(
        token: String,
        semester: String? = null,
    ): PerformanceResponseDto {
        return client.get(GET_PERFOMANCE) {
            parameter("token", token)
            parameter("semestr", semester.orEmpty())
        }.body()
    }
}
