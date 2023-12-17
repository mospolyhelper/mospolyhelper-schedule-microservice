package com.mospolytech.data.schedule.converters.lessons

import com.mospolytech.data.schedule.converters.dateTime.ApiDateTimeData

class ApiLessonData(
    val titles: MutableSet<String> = mutableSetOf(),
    val types: MutableSet<Pair<String, String>> = mutableSetOf(),
    val groups: MutableSet<Pair<String, Int>> = mutableSetOf(),
    val teachers: MutableSet<String> = mutableSetOf(),
    val places: MutableSet<String> = mutableSetOf(),
    val dateTimes: MutableSet<ApiDateTimeData> = mutableSetOf(),
)
