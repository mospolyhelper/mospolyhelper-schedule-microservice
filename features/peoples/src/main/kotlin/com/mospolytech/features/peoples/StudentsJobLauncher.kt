package com.mospolytech.features.peoples

import com.mospolytech.features.base.JobSchedulerManager
import org.quartz.*

class StudentsJobLauncher(
    private val jobSchedulerManager: JobSchedulerManager,
) {
    fun launch() {
        val job = jobSchedulerManager.createNewJob<StudentsUpdateJob>(
            key = StudentsUpdateJob.KEY,
            group = StudentsUpdateJob.GROUP,
        )

        val trigger = jobSchedulerManager.createTrigger(
            triggerId = "UpdateStudentsJob",
            group = StudentsUpdateJob.GROUP,
            schedule = JobSchedulerManager.studentsUpdateCron,
        )

        // Tell quartz to schedule the job using our trigger
        jobSchedulerManager.scheduler.scheduleJob(job, trigger)
    }

    fun launchNow() {
        jobSchedulerManager.launchJobNow(StudentsUpdateJob.KEY, StudentsUpdateJob.GROUP)
    }
}
