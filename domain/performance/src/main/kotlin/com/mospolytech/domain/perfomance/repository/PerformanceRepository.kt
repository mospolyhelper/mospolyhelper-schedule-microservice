package com.mospolytech.domain.perfomance.repository

import com.mospolytech.domain.perfomance.model.Performance
import com.mospolytech.domain.perfomance.model.PerformancePeriod

interface PerformanceRepository {
    suspend fun getPeriods(token: String): Result<List<PerformancePeriod>>

    suspend fun getPerformance(
        id: String?,
        token: String,
    ): Result<List<Performance>>
}
