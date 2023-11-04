package com.mospolytech.features.schedule

import com.mospolytech.features.base.JobSchedulerManager
import org.quartz.*

class ScheduleJobLauncher(
    private val jobSchedulerManager: JobSchedulerManager,
) {
    fun launch() {
        val triggerId = "UpdateScheduleJobEveryDay"

        // If a job exists, delete it!
        val jobScheduler = jobSchedulerManager.scheduler
        val jobKey = JobKey.jobKey(ScheduleUpdateJob.KEY, ScheduleUpdateJob.GROUP)
        jobScheduler.deleteJob(jobKey)

        val job: JobDetail =
            JobBuilder.newJob(ScheduleUpdateJob::class.java)
                .withIdentity(ScheduleUpdateJob.KEY, ScheduleUpdateJob.GROUP)
                .build()

        val trigger: Trigger =
            TriggerBuilder.newTrigger()
                .withIdentity(triggerId, ScheduleUpdateJob.GROUP)
                .withSchedule(
                    CronScheduleBuilder.monthlyOnDayAndHourAndMinute(4, 1, 10),
                )
                .build()

        // Tell quartz to schedule the job using our trigger
        jobSchedulerManager.scheduler.scheduleJob(job, trigger)
    }
}
