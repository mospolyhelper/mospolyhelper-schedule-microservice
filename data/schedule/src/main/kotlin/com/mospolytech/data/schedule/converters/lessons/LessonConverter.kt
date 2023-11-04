package com.mospolytech.data.schedule.converters.lessons

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.converters.dateTime.LessonDateTimeConverter
import com.mospolytech.data.schedule.converters.groups.LessonGroupsConverter
import com.mospolytech.data.schedule.converters.places.LessonPlacesConverter
import com.mospolytech.data.schedule.converters.subjects.LessonSubjectConverter
import com.mospolytech.data.schedule.converters.teachers.LessonTeachersConverter
import com.mospolytech.data.schedule.converters.types.LessonTypeConverter
import com.mospolytech.data.schedule.model.db.LessonToGroupsDb
import com.mospolytech.data.schedule.model.db.LessonToLessonDateTimesDb
import com.mospolytech.data.schedule.model.db.LessonToPlacesDb
import com.mospolytech.data.schedule.model.db.LessonToTeachersDb
import com.mospolytech.data.schedule.model.db.LessonsDb
import com.mospolytech.data.schedule.model.response.ApiGroup
import com.mospolytech.data.schedule.model.response.ApiLesson
import org.jetbrains.exposed.sql.batchInsert
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class LessonConverter(
    private val lessonSubjectConverter: LessonSubjectConverter,
    private val lessonTypeConverter: LessonTypeConverter,
    private val teachersConverter: LessonTeachersConverter,
    private val groupsConverter: LessonGroupsConverter,
    private val placesConverter: LessonPlacesConverter,
    private val lessonDateTimeConverter: LessonDateTimeConverter,
) {
    private val logger = LoggerFactory.getLogger("com.mospolytech.data.schedule.converters.lessons.LessonConverter")

    private val lessonCache = HashMap<ApiLessonCache, UUID>()

    private val lessonToTeacher = HashSet<Pair<UUID, String>>()
    private val lessonToGroup = HashSet<Pair<UUID, String>>()
    private val lessonToPlace = HashSet<Pair<UUID, UUID>>()
    private val lessonToDateTime = HashSet<Pair<UUID, UUID>>()

    fun cacheLesson(
        apiLesson: ApiLesson,
        apiGroups: List<ApiGroup>,
        timesId: List<String>,
    ) {
        val subjectId = lessonSubjectConverter.getCachedId(apiLesson.sbj)
        val typeId = lessonTypeConverter.getCachedId(apiLesson.type, apiLesson.sbj)
        val teachersId = teachersConverter.getCachedIds(apiLesson.teacher)
        val groupsId = groupsConverter.getCachedIds(apiGroups)
        val placesId = placesConverter.getCachedIds(apiLesson.auditories)

        val cacheKey =
            ApiLessonCache(
                typeId = typeId,
                subjectId = subjectId,
                teachersId = teachersId,
                placesId = placesId,
                timesId = timesId,
            )
        var lessonId = lessonCache[cacheKey]

        // Если не нашли в мапе (для мёрджа групп), то создаём новый UUID
        if (lessonId == null) {
            lessonId = UUID.randomUUID()

            groupsId.forEach {
                lessonToGroup.add(lessonId to it)
            }
            teachersId.forEach {
                lessonToTeacher.add(lessonId to it)
            }
            placesId.forEach {
                lessonToPlace.add(lessonId to UUID.fromString(it))
            }
            timesId.forEach {
                lessonToDateTime.add(lessonId to UUID.fromString(it))
            }

            lessonCache[cacheKey] = lessonId
        } else {
            // Так как выполняем мёрдж групп, то добавлять группу нужно в обеих ветках
            groupsId.forEach {
                lessonToGroup.add(lessonId to it)
            }
        }
    }

    suspend fun pushAllLessons() {
        logger.debug("Lessons count = ${lessonCache.size}")
        logger.debug("Lessons teachers = ${lessonToTeacher.size}")
        logger.debug("Lessons groups = ${lessonToGroup.size}")
        logger.debug("Lesson places count = ${lessonToPlace.size}")
        logger.debug("Lesson dateTimes count = ${lessonToDateTime.size}")
        MosPolyDb.transaction {
            LessonsDb.batchInsert(lessonCache.entries) { (lessonCache, lessonId) ->
                this[LessonsDb.id] = lessonId
                this[LessonsDb.type] = UUID.fromString(lessonCache.typeId)
                this[LessonsDb.subject] = UUID.fromString(lessonCache.subjectId)
            }

            LessonToTeachersDb.batchInsert(lessonToTeacher) {
                this[LessonToTeachersDb.lesson] = it.first
                this[LessonToTeachersDb.teacher] = it.second
            }
            LessonToGroupsDb.batchInsert(lessonToGroup) {
                this[LessonToGroupsDb.lesson] = it.first
                this[LessonToGroupsDb.group] = it.second
            }
            LessonToPlacesDb.batchInsert(lessonToPlace) {
                this[LessonToPlacesDb.lesson] = it.first
                this[LessonToPlacesDb.place] = it.second
            }
            LessonToLessonDateTimesDb.batchInsert(lessonToDateTime) {
                this[LessonToLessonDateTimesDb.lesson] = it.first
                this[LessonToLessonDateTimesDb.time] = it.second
            }
        }
        logger.debug("All lesson pushed")
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
        clearLessonCache()
    }

    private fun clearLessonCache() {
        lessonCache.clear()
        lessonToTeacher.clear()
        lessonToGroup.clear()
        lessonToPlace.clear()
        lessonToDateTime.clear()
    }
}
