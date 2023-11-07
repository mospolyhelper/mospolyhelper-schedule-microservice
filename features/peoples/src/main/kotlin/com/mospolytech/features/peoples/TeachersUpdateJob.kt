package com.mospolytech.features.peoples

import com.mospolytech.domain.peoples.repository.TeachersRepository
import kotlinx.coroutines.runBlocking
import org.quartz.Job
import org.quartz.JobExecutionContext

class TeachersUpdateJob(
    private val teachersRepository: TeachersRepository,
) : Job {
    override fun execute(context: JobExecutionContext?) =
        runBlocking {
            teachersRepository.updateData(false)
        }

    companion object {
        const val KEY = "UpdateTeachersJob"
        const val GROUP = "autoupdate"
    }
}
