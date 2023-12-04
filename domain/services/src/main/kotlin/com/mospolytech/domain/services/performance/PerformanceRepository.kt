package com.mospolytech.domain.services.performance

interface PerformanceRepository {
    suspend fun getPeriods(token: String): Result<List<PerformancePeriod>>

    suspend fun getPerformance(
        id: String?,
        token: String,
    ): Result<List<Performance>>
}
