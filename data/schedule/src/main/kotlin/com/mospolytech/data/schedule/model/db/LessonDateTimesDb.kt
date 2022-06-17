package com.mospolytech.data.schedule.model.db

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.time
import org.jetbrains.exposed.sql.kotlin.datetime.date

object LessonDateTimesDb : UUIDTable() {
    val startDate = date("start_date")
    val endDate = date("end_date").nullable()
    val startTime = time("start_time")
    val endTime = time("end_time")

    init {
        uniqueIndex(startDate, endDate, startTime, endTime)
    }
}