package com.mospolytech.features.peoples

import com.mospolytech.features.base.JobSchedulerManager

class TeachersJobLauncher(
    private val jobSchedulerManager: JobSchedulerManager,
) {
    fun launch() {
        val job = jobSchedulerManager.createNewJob<TeachersUpdateJob>(
            key = TeachersUpdateJob.KEY,
            group = TeachersUpdateJob.GROUP,
        )

        val trigger = jobSchedulerManager.createTrigger(
            triggerId = "UpdateTeachersJob",
            group = TeachersUpdateJob.GROUP,
            schedule = JobSchedulerManager.teachersUpdateCron,
        )

        // Tell quartz to schedule the job using our trigger
        jobSchedulerManager.scheduler.scheduleJob(job, trigger)
    }

    fun launchNow() {
        jobSchedulerManager.launchJobNow(TeachersUpdateJob.KEY, TeachersUpdateJob.GROUP)
    }
}
