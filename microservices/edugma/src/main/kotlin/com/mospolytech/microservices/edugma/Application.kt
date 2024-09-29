package com.mospolytech.microservices.edugma

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.features.auth.authRoutesV1
import com.mospolytech.features.base.JobSchedulerManager
import com.mospolytech.features.base.plugins.*
import com.mospolytech.features.peoples.StudentsJobLauncher
import com.mospolytech.features.peoples.TeachersJobLauncher
import com.mospolytech.features.peoples.peoplesRoutesV1
import com.mospolytech.features.schedule.ScheduleJobLauncher
import com.mospolytech.features.schedule.scheduleDataConversion
import com.mospolytech.features.schedule.scheduleRoutes
import com.mospolytech.features.services.applications.applicationsRoutesV1
import com.mospolytech.features.services.payments.paymentsRoutesV1
import com.mospolytech.features.services.performance.performanceRoutesV1
import com.mospolytech.features.services.personal.personalRoutesV1
import io.ktor.server.application.*
import io.ktor.server.plugins.dataconversion.*
import org.koin.ktor.ext.get

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureDi(appModules)
    configureSecurity()
    configureRouting()
    configureSerialization()
    configureMonitoring(get())
    configureHTTP()
    setRoutes()
    setDataConversions()
    initDb()
    initJobScheduler(get())
}

fun Application.setRoutes() {
    authRoutesV1(get(), get(), get(), get(), get())
    applicationsRoutesV1(get())
    paymentsRoutesV1(get())
    personalRoutesV1(get())
    performanceRoutesV1(get())
    peoplesRoutesV1(get(), get(), get(), get(), get())
    scheduleRoutes(get(), get(), get(), get(), get(), get(), get())
}

fun Application.setDataConversions() {
    install(DataConversion) {
        scheduleDataConversion()
    }
}

fun Application.initDb() {
    MosPolyDb.connectAndMigrate(get())
}

fun Application.initJobScheduler(scheduler: JobSchedulerManager) {
    scheduler.startScheduler()

    val scheduleJobLauncher = get<ScheduleJobLauncher>()
    scheduleJobLauncher.launch()

    val studentsScheduler = get<StudentsJobLauncher>()
    studentsScheduler.launch()

    val teachersScheduler = get<TeachersJobLauncher>()
    teachersScheduler.launch()
}
