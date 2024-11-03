package com.mospolytech.features.schedule

import com.mospolytech.features.base.JobSchedulerManager

class ScheduleJobLauncher(
    private val jobSchedulerManager: JobSchedulerManager,
) {
    fun launch() {
        val job = jobSchedulerManager.createNewJob<ScheduleUpdateJob>(
            key = ScheduleUpdateJob.KEY,
            group = ScheduleUpdateJob.GROUP,
        )

        val trigger = jobSchedulerManager.createTrigger(
            triggerId = "UpdateScheduleJob",
            group = ScheduleUpdateJob.GROUP,
            schedule = JobSchedulerManager.scheduleUpdateCron,
        )

        // Tell quartz to schedule the job using our trigger
        jobSchedulerManager.scheduler.scheduleJob(job, trigger)
    }

    fun launchNow() {
        jobSchedulerManager.launchJobNow(ScheduleUpdateJob.KEY, ScheduleUpdateJob.GROUP)
    }
}
