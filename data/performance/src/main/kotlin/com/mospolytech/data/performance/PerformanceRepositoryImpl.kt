package com.mospolytech.data.performance

import com.mospolytech.domain.perfomance.model.Performance
import com.mospolytech.domain.perfomance.repository.PerformanceRepository

class PerformanceRepositoryImpl(private val service: PerformanceService): PerformanceRepository {

    override suspend fun getCourses(token: String): Result<List<Int>> {
        return runCatching {
            listOf(1,2,3,4)
        }
    }

    override suspend fun getSemesters(token: String): Result<List<Int>> {
        return runCatching {
            listOf(1,2,3,4,5,6,7,8)
        }
    }


    override suspend fun getPerformance(semester: Int, token: String): Result<List<Performance>> {
        return runCatching {
            val performanceResponse = service.getPerformanceInfo(token, semester)
            performanceResponse.academicPerformance.map { it.toModel() }
        }
    }
}