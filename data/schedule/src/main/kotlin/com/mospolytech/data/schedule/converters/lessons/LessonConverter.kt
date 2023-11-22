package com.mospolytech.data.schedule.converters.lessons

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.converters.groups.LessonGroupsConverter
import com.mospolytech.data.schedule.converters.places.LessonPlacesConverter
import com.mospolytech.data.schedule.converters.subjects.LessonSubjectConverter
import com.mospolytech.data.schedule.converters.teachers.LessonTeachersConverter
import com.mospolytech.data.schedule.converters.types.LessonTypeConverter
import com.mospolytech.data.schedule.model.db.LessonToGroupsDb
import com.mospolytech.data.schedule.model.db.LessonToPlacesDb
import com.mospolytech.data.schedule.model.db.LessonToTeachersDb
import com.mospolytech.data.schedule.model.db.LessonsDb
import com.mospolytech.data.schedule.model.db.RecurrenceDb
import com.mospolytech.data.schedule.model.db.toByte
import com.mospolytech.data.schedule.model.response.ApiGroup
import com.mospolytech.data.schedule.model.response.ApiLesson
import com.mospolytech.domain.schedule.model.pack.LessonDateTime
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
) {
    private val logger = LoggerFactory.getLogger("com.mospolytech.data.schedule.converters.lessons.LessonConverter")

    private val lessonCache = HashMap<ApiLessonCache, UUID>()

    private val lessonToTeacher = HashSet<Pair<UUID, String>>()
    private val lessonToGroup = HashSet<Pair<UUID, String>>()
    private val lessonToPlace = HashSet<Pair<UUID, UUID>>()
    private val lessonToDateTime = HashSet<Pair<UUID, String>>()

    fun cacheLesson(
        apiLesson: ApiLesson,
        apiGroups: List<ApiGroup>,
        startDateTime: LessonDateTime,
        endDateTime: LessonDateTime,
        recurrence: String?,
    ) {
        val (subjectId, subgroup) = lessonSubjectConverter.getCachedId(apiLesson.sbj)
        val (type, importance) = lessonTypeConverter.getCached(apiLesson.type, apiLesson.sbj)
        val teachersId = teachersConverter.getCachedIds(apiLesson.teacher)
        val groupsId = groupsConverter.getCachedIds(apiGroups)
        val placesId = placesConverter.getCachedIds(apiLesson.auditories)

        val cacheKey =
            ApiLessonCache(
                type = type,
                subgroup = subgroup,
                subjectId = subjectId,
                teachersId = teachersId,
                placesId = placesId,
                start = startDateTime,
                end = endDateTime,
                importance = importance,
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
            recurrence?.let {
                lessonToDateTime.add(lessonId to it)
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
                this[LessonsDb.type] = lessonCache.type
                this[LessonsDb.subgroup] = lessonCache.subgroup
                this[LessonsDb.subject] = lessonCache.subjectId
                this[LessonsDb.startDateTime] = lessonCache.start.dateTime
                this[LessonsDb.endDateTime] = lessonCache.end.dateTime
                this[LessonsDb.importance] = lessonCache.importance.toByte()
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
            RecurrenceDb.batchInsert(lessonToDateTime) {
                this[RecurrenceDb.lesson] = it.first
                this[RecurrenceDb.recurrence] = it.second
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
    }

    fun clearCache() {
        lessonSubjectConverter.clearCache()
        lessonTypeConverter.clearCache()
        teachersConverter.clearCache()
        groupsConverter.clearCache()
        placesConverter.clearCache()
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
