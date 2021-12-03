package com.mospolytech.mph.domain.schedule.repository

interface ScheduleRepository {
    suspend fun getSchedule()
}