package com.mospolytech.domain.perfomance.repository

import com.mospolytech.domain.perfomance.model.Performance

interface PerformanceRepository {
    suspend fun getCourses(token: String): Result<List<Int>>
    suspend fun getSemesters(token: String): Result<List<Int>>
    suspend fun getPerformance(semester: Int, token: String): Result<List<Performance>>
}