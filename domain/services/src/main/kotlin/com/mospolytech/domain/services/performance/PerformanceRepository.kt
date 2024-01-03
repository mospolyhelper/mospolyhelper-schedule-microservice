package com.mospolytech.domain.services.performance

interface PerformanceRepository {
    suspend fun getPerformance(
        id: String?,
        token: String,
    ): Result<PerformanceApi>
}
