package com.mospolytech.domain.schedule.model.schedule_info

@kotlinx.serialization.Serializable
class ScheduleObject(
    val id: String,
    val title: String,
    val description: String,
    val avatar: String?
)