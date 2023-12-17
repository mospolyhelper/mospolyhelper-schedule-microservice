package com.mospolyteh.data.services.performance

import com.mospolytech.domain.services.performance.Performance
import com.mospolytech.domain.services.performance.PerformancePeriod
import com.mospolytech.domain.services.performance.PerformanceRepository
import com.mospolytech.domain.services.personal.PersonalRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

class PerformanceRepositoryImpl(
    private val service: PerformanceService,
    private val personalRepository: PersonalRepository,
) : PerformanceRepository {
    companion object {
        private const val FEBRUARY = 2
        private const val JULY = 7
    }

    override suspend fun getPeriods(token: String): Result<List<PerformancePeriod>> {
        return runCatching {
            val course = personalRepository.getCourse(token).getOrDefault(1)
            val month = Clock.System.now().toLocalDateTime(TimeZone.UTC).month.number
            val maxSemester =
                if (month in FEBRUARY..JULY) {
                    course * 2
                } else {
                    course * 2 - 1
                }
            (1..maxSemester).map {
                PerformancePeriod(
                    id = it.toString(),
                    title = "$it-й семестр",
                )
            }
        }
    }

    override suspend fun getPerformance(
        id: String?,
        token: String,
    ): Result<List<Performance>> {
        return runCatching {
            val performanceResponse = service.getPerformanceInfo(token, id)
            performanceResponse.academicPerformance.map { it.toModel() }
        }
    }
}
