package com.mospolytech.features.base

import com.mospolytech.domain.base.AppConfig
import com.mospolytech.features.base.utils.DiJobFactory
import org.quartz.CronScheduleBuilder
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Scheduler
import org.quartz.SchedulerFactory
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import java.util.*

class JobSchedulerManager(config: AppConfig) {
    var scheduler: Scheduler

    init {
        val props = Properties()
        props["org.quartz.scheduler.instanceName"] = "MospolytechScheduler"
        props["org.quartz.threadPool.threadCount"] = "3"

        props["org.quartz.dataSource.mySql.driver"] = config.driver
        props["org.quartz.dataSource.mySql.URL"] = config.url
        props["org.quartz.dataSource.mySql.user"] = config.login
        props["org.quartz.dataSource.mySql.password"] = config.password
        props["org.quartz.dataSource.mySql.maxConnections"] = "10"

        props["org.quartz.jobStore.class"] = "org.quartz.impl.jdbcjobstore.JobStoreTX"
        props["org.quartz.jobStore.driverDelegateClass"] = "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate"
        props["org.quartz.jobStore.tablePrefix"] = "QRTZ_"
        props["org.quartz.jobStore.dataSource"] = "mySql"

        props["org.quartz.plugin.triggHistory.class"] = "org.quartz.plugins.history.LoggingTriggerHistoryPlugin"
        props["org.quartz.plugin.triggHistory.triggerFiredMessage"] =
            """Trigger {1}.{0} fired job {6}.{5} at: {4, date, HH:mm:ss MM/dd/yyyy}"""
        props["org.quartz.plugin.triggHistory.triggerCompleteMessage"] =
            """Trigger {1}.{0} completed firing job {6}.{5} at {4, date, HH:mm:ss MM/dd/yyyy}"""

        val schedulerFactory: SchedulerFactory = StdSchedulerFactory(props)

        scheduler =
            schedulerFactory.scheduler.apply {
                setJobFactory(DiJobFactory())
            }
    }

    fun startScheduler() {
        scheduler.start()
    }

    fun launchJobNow(key: String, group: String) {
        val jobKey = JobKey.jobKey(key, group)
        scheduler.triggerJob(jobKey)
    }

    inline fun <reified T : Job> createNewJob(key: String, group: String): JobDetail {
        // If a job exists, delete it!
        val jobKey = JobKey.jobKey(key, group)
        scheduler.deleteJob(jobKey)

        val job: JobDetail =
            JobBuilder.newJob(T::class.java)
                .withIdentity(key, group)
                .build()

        return job
    }

    fun createTrigger(triggerId: String, group: String, schedule: CronScheduleBuilder): Trigger {
        val trigger: Trigger =
            TriggerBuilder.newTrigger()
                .withIdentity(triggerId, group)
                .withSchedule(schedule)
                .build()

        return trigger
    }

    companion object {
        val scheduleUpdateCron
            get() = CronScheduleBuilder.dailyAtHourAndMinute(0, 32)
        val studentsUpdateCron
            get() = CronScheduleBuilder.monthlyOnDayAndHourAndMinute(4, 1, 44)
        val teachersUpdateCron
            get() = CronScheduleBuilder.monthlyOnDayAndHourAndMinute(5, 1, 44)
    }
}
