package com.mospolytech.features.schedule

import com.mospolytech.domain.schedule.repository.ScheduleRepository
import kotlinx.coroutines.runBlocking
import org.quartz.Job
import org.quartz.JobExecutionContext

class ScheduleUpdateJob(
    private val scheduleRepository: ScheduleRepository,
) : Job {
    override fun execute(context: JobExecutionContext?) = runBlocking {
        scheduleRepository.updateData(false)
    }

    companion object {
        const val KEY = "UpdateScheduleJob"
        const val GROUP = "autoupdate"
    }
}
