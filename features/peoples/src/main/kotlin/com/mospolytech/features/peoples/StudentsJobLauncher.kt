package com.mospolytech.features.peoples

import com.mospolytech.features.base.JobSchedulerManager
import org.quartz.*

class StudentsJobLauncher(
    private val jobSchedulerManager: JobSchedulerManager,
) {
    fun launch() {
        val triggerId = "UpdateStudentsJob"

        // If a job exists, delete it!
        val jobScheduler = jobSchedulerManager.scheduler
        val jobKey = JobKey.jobKey(StudentsUpdateJob.KEY, StudentsUpdateJob.GROUP)
        jobScheduler.deleteJob(jobKey)

        val job: JobDetail =
            JobBuilder.newJob(StudentsUpdateJob::class.java)
                .withIdentity(StudentsUpdateJob.KEY, StudentsUpdateJob.GROUP)
                .build()

        val trigger: Trigger =
            TriggerBuilder.newTrigger()
                .withIdentity(triggerId, StudentsUpdateJob.GROUP)
                .withSchedule(
                    CronScheduleBuilder.monthlyOnDayAndHourAndMinute(4, 1, 44),
                )
                .build()

        // Tell quartz to schedule the job using our trigger
        jobSchedulerManager.scheduler.scheduleJob(job, trigger)
    }

    fun launchNow() {
        val jobScheduler = jobSchedulerManager.scheduler
        val jobKey = JobKey.jobKey(StudentsUpdateJob.KEY, StudentsUpdateJob.GROUP)
        jobScheduler.triggerJob(jobKey)
    }
}
