package com.mospolytech.data.performance

import com.mospolytech.domain.perfomance.model.SemestersWithCourse
import com.mospolytech.domain.perfomance.model.Performance
import com.mospolytech.domain.perfomance.repository.PerformanceRepository
import java.time.Month
import kotlin.math.ceil
import kotlin.math.roundToInt

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

    override suspend fun getCoursesWithSemesters(token: String): Result<SemestersWithCourse> {
        return runCatching {
            val performanceResponse = service.getPerformanceInfo(token)
            val lastExamMonth =
                performanceResponse.academicPerformance.map { it.toModel() }.first { it.date != null }.date!!.month
            val maxCourse = performanceResponse.academicPerformance.maxOfOrNull { it.course } ?: 0
            var maxSemester = maxCourse * 2 - 1
            if (lastExamMonth.value > Month.FEBRUARY.value && lastExamMonth.value < Month.SEPTEMBER.value) maxSemester++
            val coursesWithSemesters = mutableMapOf<Int, Int>().apply {
                val semestersInCourse = ceil(maxSemester.toDouble()/maxCourse.toDouble()).roundToInt()
                for (semester in 1..maxSemester) {
                    put(semester, ceil(semester.toDouble() / semestersInCourse).roundToInt())
                }
            }
            SemestersWithCourse(coursesWithSemesters)
        }
    }

    override suspend fun getPerformance(semester: String?, token: String): Result<List<Performance>> {
        return runCatching {
            val performanceResponse = service.getPerformanceInfo(token, semester)
            performanceResponse.academicPerformance.map { it.toModel() }
        }
    }
}