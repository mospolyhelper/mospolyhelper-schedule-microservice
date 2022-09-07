package com.mospolytech.data.performance

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class PerformanceService(
    private val client: HttpClient
) {
    companion object {
        private const val BaseUrl = "https://e.mospolytech.ru"
        private const val ApiUrl = "$BaseUrl/old/lk_api.php"

        private const val GetPerformance = "$ApiUrl?getAcademicPerformance="
    }

    suspend fun getPerformanceInfo(token: String, semester: String? = null): PerformanceResponseDto {
        return client.get(GetPerformance) {
            parameter("token", token)
            parameter("semestr", semester.orEmpty())
        }.body()
    }
}
