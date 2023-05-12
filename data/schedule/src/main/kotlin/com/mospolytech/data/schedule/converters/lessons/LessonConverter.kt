package com.mospolytech.data.schedule.converters.lessons

import com.mospolytech.data.schedule.converters.dateTime.LessonDateTimeConverter
import com.mospolytech.data.schedule.converters.groups.LessonGroupsConverter
import com.mospolytech.data.schedule.converters.places.LessonPlacesConverter
import com.mospolytech.data.schedule.converters.subjects.LessonSubjectConverter
import com.mospolytech.data.schedule.converters.teachers.LessonTeachersConverter
import com.mospolytech.data.schedule.converters.types.LessonTypeConverter
import com.mospolytech.data.schedule.model.response.ApiGroup
import com.mospolytech.data.schedule.model.response.ApiLesson
import com.mospolytech.data.schedule.remote.LessonsRemoteDS
import org.slf4j.LoggerFactory

class LessonConverter(
    private val lessonSubjectConverter: LessonSubjectConverter,
    private val lessonTypeConverter: LessonTypeConverter,
    private val teachersConverter: LessonTeachersConverter,
    private val groupsConverter: LessonGroupsConverter,
    private val placesConverter: LessonPlacesConverter,
    private val lessonsRemoteDS: LessonsRemoteDS,
    private val lessonDateTimeConverter: LessonDateTimeConverter,
) {

    private val logger = LoggerFactory.getLogger("com.mospolytech.data.schedule.converters.lessons.LessonConverter")

    suspend fun convertLesson(
        apiLesson: ApiLesson,
        apiGroups: List<ApiGroup>,
        timesId: List<String>,
    ): String {
        val subjectId = lessonSubjectConverter.getCachedId(apiLesson.sbj)
        val typeId = lessonTypeConverter.getCachedId(apiLesson.type, apiLesson.sbj)
        val teachersId = teachersConverter.getCachedIds(apiLesson.teacher)
        val groupsId = groupsConverter.getCachedIds(apiGroups)
        val placesId = placesConverter.getCachedIds(apiLesson.auditories)

        return lessonsRemoteDS.add(
            typeId = typeId,
            subjectId = subjectId,
            teachersId = teachersId,
            groupsId = groupsId,
            placesId = placesId,
            lessonDateTimesId = timesId,
        )
    }

    suspend fun cacheAll(lessonData: ApiLessonData) {
        logger.debug("Cache subjects")
        lessonSubjectConverter.cacheAll(lessonData.titles)
        logger.debug("Cache types")
        lessonTypeConverter.cacheAll(lessonData.types)
        logger.debug("Cache teachers")
        teachersConverter.cacheAll(lessonData.teachers)
        logger.debug("Cache groups")
        groupsConverter.cacheAll(lessonData.groups)
        logger.debug("Cache places")
        placesConverter.cacheAll(lessonData.places)
        logger.debug("Cache dateTimes")
        lessonDateTimeConverter.cacheAll(lessonData.dateTimes)
    }

    fun clearCache() {
        lessonSubjectConverter.clearCache()
        lessonTypeConverter.clearCache()
        teachersConverter.clearCache()
        groupsConverter.clearCache()
        placesConverter.clearCache()
        lessonDateTimeConverter.clearCache()
    }
}
