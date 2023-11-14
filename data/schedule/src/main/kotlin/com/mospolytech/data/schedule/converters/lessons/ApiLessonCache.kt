package com.mospolytech.data.schedule.converters.lessons

import com.mospolytech.domain.schedule.model.lessonType.Importance
import com.mospolytech.domain.schedule.model.pack.LessonDateTime

data class ApiLessonCache(
    val type: String,
    val subgroup: Byte?,
    val subjectId: String,
    val teachersId: List<String>,
    val placesId: List<String>,
    val start: LessonDateTime,
    val end: LessonDateTime,
    val importance: Importance,
)
