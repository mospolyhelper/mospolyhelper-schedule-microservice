package com.mospolytech.data.schedule.remote

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.*
import org.jetbrains.exposed.sql.*
import java.util.*

class LessonsRemoteDS {
    private val map = mutableMapOf<LessonCacheKey, String>()

    private fun <T> listContentEquals(l1: List<T>, l2: List<T>): Boolean {
        if (l1.size != l2.size) return false
        return l1.all { it in l2 }
    }

    suspend fun add(
        typeId: String,
        subjectId: String,
        teachersId: List<String>,
        groupsId: List<String>,
        placesId: List<String>,
        lessonDateTimesId: List<String>
    ): String {
        return MosPolyDb.transaction {
            val cacheKey = LessonCacheKey(
                typeId = typeId,
                subjectId = subjectId,
                teachersId = teachersId,
                placesId = placesId,
                lessonDateTimesId = lessonDateTimesId
            )

            var id = map[cacheKey]

            if (id == null) {
                id = firstOrNullId(
                    typeId,
                    subjectId,
                    teachersId,
                    groupsId,
                    placesId,
                    lessonDateTimesId
                )
            }

            if (id != null) {
                groupsId.forEach { groupId ->
                    val group = LessonToGroupsDb.select {
                        LessonToGroupsDb.lesson eq UUID.fromString(id) and
                                (LessonToGroupsDb.group eq groupId)
                    }.firstOrNull()

                    if (group == null) {
                        LessonToGroupsDb.insert {
                            it[this.lesson] = UUID.fromString(id)
                            it[this.group] = groupId
                        }
                    }
                }
            }


            if (id == null) {
                id = LessonsDb.insertAndGetId {
                    it[type] = UUID.fromString(typeId)
                    it[subject] = UUID.fromString(subjectId)
                }.value.toString()

                LessonToTeachersDb.batchInsert(teachersId) {
                    this[LessonToTeachersDb.lesson] = UUID.fromString(id)
                    this[LessonToTeachersDb.teacher] = it
                }
                LessonToGroupsDb.batchInsert(groupsId) {
                    this[LessonToGroupsDb.lesson] = UUID.fromString(id)
                    this[LessonToGroupsDb.group] = it
                }
                LessonToPlacesDb.batchInsert(placesId) {
                    this[LessonToPlacesDb.lesson] = UUID.fromString(id)
                    this[LessonToPlacesDb.place] = UUID.fromString(it)
                }
                LessonToLessonDateTimesDb.batchInsert(lessonDateTimesId) {
                    this[LessonToLessonDateTimesDb.lesson] = UUID.fromString(id)
                    this[LessonToLessonDateTimesDb.time] = UUID.fromString(it)
                }
            }

            map[cacheKey] = id

            id
        }
    }

    private suspend fun firstOrNullId(
        typeId: String,
        subjectId: String,
        teachersId: List<String>,
        groupsId: List<String>,
        placesId: List<String>,
        lessonDateTimesId: List<String>
    ): String? {
        return MosPolyDb.transaction {
            var isInitialized = false
            var resLessons = setOf<UUID>()

            fun updateResList(lessons: Set<UUID>) {
                resLessons = if (isInitialized) {
                    resLessons intersect lessons
                } else {
                    lessons
                }
                isInitialized = true
            }

            if (groupsId.isNotEmpty()) {
                val lessonsToIntersect = LessonToGroupsDb.select {
                    LessonToGroupsDb.group inList groupsId
                }.map { it[LessonToGroupsDb.lesson].value }.toSet()

                updateResList(lessonsToIntersect)
                if (isInitialized && resLessons.isEmpty()) return@transaction null
            }

            if (teachersId.isNotEmpty()) {
                val lessonsToIntersect = LessonToTeachersDb.select {
                    LessonToTeachersDb.teacher inList teachersId
                }.map { it[LessonToTeachersDb.lesson].value }.toSet()

                updateResList(lessonsToIntersect)
                if (isInitialized && resLessons.isEmpty()) return@transaction null
            }

            if (placesId.isNotEmpty()) {
                val placesId = placesId.map { UUID.fromString(it) }
                val lessonsToIntersect = LessonToPlacesDb.select {
                    LessonToPlacesDb.place inList placesId
                }.map { it[LessonToPlacesDb.lesson].value }.toSet()

                updateResList(lessonsToIntersect)
                if (isInitialized && resLessons.isEmpty()) return@transaction null
            }

            if (placesId.isNotEmpty()) {
                val lessonDateTimesId = lessonDateTimesId.map { UUID.fromString(it) }
                val lessonsToIntersect = LessonToLessonDateTimesDb.select {
                    LessonToLessonDateTimesDb.time inList lessonDateTimesId
                }.map { it[LessonToLessonDateTimesDb.lesson].value }.toSet()

                updateResList(lessonsToIntersect)
                if (isInitialized && resLessons.isEmpty()) return@transaction null
            }

            val query = LessonsDb.selectAll()

            if (resLessons.isNotEmpty()) {
                query.andWhere { LessonsDb.id inList resLessons }
            }

            query.andWhere {
                LessonsDb.subject eq  UUID.fromString(subjectId)
            }

            query.andWhere {
                LessonsDb.type eq UUID.fromString(typeId)
            }

            query.mapLazy { it[LessonsDb.id].value.toString() }.firstOrNull()
        }
    }

    private data class LessonCacheKey(
        val typeId: String,
        val subjectId: String,
        val teachersId: List<String>,
        val placesId: List<String>,
        val lessonDateTimesId: List<String>
    )
}