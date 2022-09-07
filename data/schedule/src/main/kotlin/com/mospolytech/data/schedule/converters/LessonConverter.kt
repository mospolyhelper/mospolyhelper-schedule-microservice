package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.response.ApiGroup
import com.mospolytech.data.schedule.model.response.ApiLesson
import com.mospolytech.data.schedule.remote.LessonsRemoteDS

class LessonConverter(
    private val lessonSubjectsConverter: LessonSubjectConverter,
    private val lessonTypeConverter: LessonTypeConverter,
    private val teachersConverter: LessonTeachersConverter,
    private val groupsConverter: LessonGroupsConverter,
    private val placesConverter: LessonPlacesConverter,
    private val lessonsRemoteDS: LessonsRemoteDS,
) {

    suspend fun convertLesson(
        apiLesson: ApiLesson,
        apiGroups: List<ApiGroup>,
        timesId: List<String>
    ): String {
        val subjectId = lessonSubjectsConverter.convertTitle(apiLesson.sbj)
        val typeId = lessonTypeConverter.convertType(apiLesson.type, apiLesson.sbj)
        val teachersId = teachersConverter.convertTeachers(apiLesson.teacher)
        val groupsId = groupsConverter.convertGroups(apiGroups)
        val placesId = placesConverter.convertPlaces(apiLesson.auditories)

        return lessonsRemoteDS.add(
            typeId = typeId,
            subjectId = subjectId,
            teachersId = teachersId,
            groupsId = groupsId,
            placesId = placesId,
            lessonDateTimesId = timesId
        )
    }
}
