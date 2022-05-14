package com.mospolytech.data.performance

import com.mospolytech.domain.perfomance.model.Performance
import com.mospolytech.domain.perfomance.repository.PerformanceRepository
import java.time.Month

class PerformanceRepositoryImpl(private val service: PerformanceService): PerformanceRepository {

    override suspend fun getCourses(token: String): Result<List<Int>> {
        return runCatching {
            val performanceResponse = service.getPerformanceInfo(token)
            val maxCourse = performanceResponse.academicPerformance.maxOfOrNull { it.course } ?: 0
            List(maxCourse) { it + 1 }
        }
    }

    override suspend fun getSemesters(token: String): Result<List<Int>> {
        return runCatching {
            val performanceResponse = service.getPerformanceInfo(token)
            val lastExamMonth = performanceResponse.academicPerformance.map { it.toModel() }.first { it.date != null }.date!!.month
            val maxCourse = performanceResponse.academicPerformance.maxOfOrNull { it.course } ?: 0
            var maxSemester = maxCourse * 2
            if (lastExamMonth.value <= Month.FEBRUARY.value) maxSemester--
            List(maxSemester) { it + 1 }
        }
    }

    override suspend fun getPerformance(semester: String?, token: String): Result<List<Performance>> {
        return runCatching {
            val performanceResponse = service.getPerformanceInfo(token, semester)
            performanceResponse.academicPerformance.map { it.toModel() }
        }
    }
}