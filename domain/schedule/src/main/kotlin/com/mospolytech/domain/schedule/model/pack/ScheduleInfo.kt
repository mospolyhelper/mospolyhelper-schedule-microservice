package com.mospolytech.domain.schedule.model.pack

import com.mospolytech.domain.peoples.model.GroupShort
import com.mospolytech.domain.schedule.model.lessonSubject.CompactLessonSubjectInfo
import com.mospolytech.domain.schedule.model.lessonType.LessonTypeInfo
import com.mospolytech.domain.schedule.model.place.CompactPlaceInfo
import com.mospolytech.domain.schedule.model.teacher.CompactLessonTeacherInfo
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleInfo(
    val typesInfo: List<LessonTypeInfo>,
    val subjectsInfo: List<CompactLessonSubjectInfo>,
    val teachersInfo: List<CompactLessonTeacherInfo>,
    val groupsInfo: List<GroupShort>,
    val placesInfo: List<CompactPlaceInfo>,
)
