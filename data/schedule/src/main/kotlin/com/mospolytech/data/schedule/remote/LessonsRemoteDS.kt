package com.mospolytech.data.schedule.remote

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.*
import com.mospolytech.data.schedule.model.entity.*
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
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
                id = LessonEntity.all()
                    .filter {
                        it.type.id.value.toString() == typeId &&
                                it.subject.id.value.toString() == subjectId &&
                                listContentEquals(it.teachers.map { it.id.value.toString() }, teachersId) &&
                                listContentEquals(it.groups.map { it.id.value.toString() }, groupsId) &&
                                listContentEquals(it.places.map { it.id.value.toString() }, placesId) &&
                                listContentEquals(it.dateTimes.map { it.id.value.toString() }, lessonDateTimesId)
                    }
                    .firstOrNull()
                    ?.toLessonModel()
                    ?.id
            }

            id?.let {
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

//    suspend fun get(id: String): LessonSubjectInfo? {
//        return MosPolyDb.transaction {
//            SubjectEntity.findById(UUID.fromString(id))?.toModel()
//        }
//    }
//
//    suspend fun getAll(): List<LessonSubjectInfo> {
//        return MosPolyDb.transaction {
//            SubjectEntity.all()
//                .orderBy(SubjectsDb.title to SortOrder.ASC)
//                .map { it.toModel() }
//        }
//    }

    private data class LessonCacheKey(
        val typeId: String,
        val subjectId: String,
        val teachersId: List<String>,
        val placesId: List<String>,
        val lessonDateTimesId: List<String>
    )
}