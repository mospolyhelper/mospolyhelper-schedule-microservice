package com.mospolytech.domain.schedule.model.pack

import com.mospolytech.domain.schedule.model.group.CompactLessonGroup
import com.mospolytech.domain.schedule.model.lesson_subject.CompactLessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.teacher.CompactLessonTeacherInfo
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleInfo(
    val typesInfo: List<LessonTypeInfo>,
    val subjectsInfo: List<CompactLessonSubjectInfo>,
    val teachersInfo: List<CompactLessonTeacherInfo>,
    val groupsInfo: List<CompactLessonGroup>,
    val placesInfo: List<PlaceInfo>,
)
