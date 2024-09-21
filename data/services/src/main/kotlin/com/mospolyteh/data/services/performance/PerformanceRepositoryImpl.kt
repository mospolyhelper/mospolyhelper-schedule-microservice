package com.mospolyteh.data.services.performance

import com.mospolytech.domain.services.performance.Performance
import com.mospolytech.domain.services.performance.PerformanceApi
import com.mospolytech.domain.services.performance.PerformancePeriod
import com.mospolytech.domain.services.performance.PerformanceRepository
import com.mospolytech.domain.services.personal.PersonalRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.todayIn

class PerformanceRepositoryImpl(
    private val service: PerformanceService,
    private val personalRepository: PersonalRepository,
) : PerformanceRepository {
    private suspend fun getPeriods(token: String): List<PerformancePeriod> {
        val course = personalRepository.getCourse(token).getOrDefault(1)
        val month = Clock.System.todayIn(TimeZone.UTC).month.number
        val maxSemester =
            if (month in FEBRUARY..JULY) {
                course * 2
            } else {
                course * 2 - 1
            }
        return (maxSemester downTo 1).map {
            PerformancePeriod(
                id = it.toString(),
                title = "$it-й семестр",
            )
        } +
            PerformancePeriod(
                id = ALL_ID,
                title = "Все",
            )
    }

    override suspend fun getPerformance(
        id: String?,
        token: String,
    ): Result<PerformanceApi> {
        return runCatching {
            val periods = getPeriods(token)

            if (periods.isEmpty()) {
                return@runCatching PerformanceApi(
                    periods = emptyList(),
                    selected = null,
                )
            }

            val firstPeriodId = periods.first().id

            val mpuId =
                when (id) {
                    ALL_ID -> ""
                    null -> firstPeriodId
                    else -> id
                }

            val performanceResponse = service.getPerformanceInfo(token, mpuId)
            val performanceList = performanceResponse.academicPerformance.map { it.toModel() }

            PerformanceApi(
                periods = periods,
                selected =
                Performance(
                    id = id ?: firstPeriodId,
                    grades = performanceList,
                ),
            )
        }
    }

    companion object {
        private const val FEBRUARY = 2
        private const val JULY = 7
        private const val ALL_ID = "all"
    }
}
