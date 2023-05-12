package com.mospolytech.data.schedule.converters.dateTime

import com.mospolytech.data.schedule.model.response.ApiLesson

data class ApiDateTimeData(
    val order: String,
    val groupIsEvening: Boolean,
    val apiLesson: ApiLesson,
    val day: String,
    val isByDate: Boolean,
)
