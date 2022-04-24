package com.mospolytech.domain.schedule.model.pack

import com.mospolytech.domain.schedule.model.group.GroupInfo
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.model.teacher.TeacherInfo
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleInfo(
    val typesInfo: List<LessonTypeInfo>,
    val subjectsInfo: List<LessonSubjectInfo>,
    val teachersInfo: List<TeacherInfo>,
    val groupsInfo: List<GroupInfo>,
    val placesInfo: List<PlaceInfo>
)