package com.mospolytech.data.schedule

import com.mospolytech.data.schedule.model.ScheduleResponse
import com.mospolytech.data.schedule.model.ScheduleSessionResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ScheduleService(
    private val client: HttpClient
) {
    companion object {
        private const val BaseUrl = "https://rasp.dmami.ru"

        private const val GetSchedule = "$BaseUrl/site/group"
        private const val GetScheduleAll = "$BaseUrl/semester.json"
        private const val GetScheduleAllSessopn = "$BaseUrl/session-file.json"

        private const val BaseUrlTeacher = "https://kaf.dmami.ru"
        private const val GetScheduleTeacher = "$BaseUrlTeacher/lessons/teacher-html"
    }

    suspend fun getScheduleByGroup(groupTitle: String, isSession: Boolean): String {
        return client.get(GetSchedule) {
            header("referer", BaseUrl)
            // For json error status if schedule is not ready instead html
            header("X-Requested-With", "XMLHttpRequest")
            parameter("group", groupTitle)
            parameter("session", if (isSession) 1 else 0)
        }.body()
    }

    suspend fun getScheduleByTeacher(teacherId: String): String {
        return client.get(GetScheduleTeacher) {
            header("referer", BaseUrlTeacher)
            header("X-Requested-With", "XMLHttpRequest")
            parameter("id", teacherId)
        }.body()
    }

    suspend fun getSchedules(): ScheduleResponse {
        return client.get(GetScheduleAll) {
            header("referer", BaseUrl)
            // For json error status if schedule is not ready instead html
            header("X-Requested-With", "XMLHttpRequest")
        }.body()
    }

    suspend fun getSchedulesSession(): ScheduleSessionResponse {
        return client.get(GetScheduleAllSessopn) {
            header("referer", BaseUrl)
            // For json error status if schedule is not ready instead html
            header("X-Requested-With", "XMLHttpRequest")
        }.body()
    }
}