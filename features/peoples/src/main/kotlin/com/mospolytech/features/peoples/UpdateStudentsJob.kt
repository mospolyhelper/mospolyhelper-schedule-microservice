package com.mospolytech.features.peoples

import com.mospolytech.domain.peoples.repository.StudentsRepository
import kotlinx.coroutines.runBlocking
import org.quartz.Job
import org.quartz.JobExecutionContext

class UpdateStudentsJob(
    private val studentsRepository: StudentsRepository,
) : Job {
    override fun execute(context: JobExecutionContext?) = runBlocking {
        studentsRepository.updateData(false)
    }
}
