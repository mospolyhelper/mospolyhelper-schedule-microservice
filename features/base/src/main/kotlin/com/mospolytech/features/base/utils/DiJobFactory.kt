package com.mospolytech.features.base.utils

import org.koin.core.component.KoinComponent
import org.quartz.Job
import org.quartz.Scheduler
import org.quartz.spi.JobFactory
import org.quartz.spi.TriggerFiredBundle

class DiJobFactory : JobFactory, KoinComponent {

    override fun newJob(bundle: TriggerFiredBundle?, scheduler: Scheduler?): Job {
        if (bundle != null) {
            val jobClass = bundle.jobDetail.jobClass

            return getKoin().get(jobClass.kotlin)
        }
        throw NotImplementedError("Job Factory error")
    }
}
