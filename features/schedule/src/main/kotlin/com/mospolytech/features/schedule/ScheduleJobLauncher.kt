package com.mospolytech.features.schedule

import com.mospolytech.features.base.JobSchedulerManager
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Trigger
import org.quartz.TriggerBuilder

class ScheduleJobLauncher(
    private val jobSchedulerManager: JobSchedulerManager,
) {
    fun launch() {
        val triggerId = "UpdateScheduleJob"

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

    fun launchNow() {
        val jobScheduler = jobSchedulerManager.scheduler
        val jobKey = JobKey.jobKey(ScheduleUpdateJob.KEY, ScheduleUpdateJob.GROUP)
        jobScheduler.triggerJob(jobKey)
    }
}
