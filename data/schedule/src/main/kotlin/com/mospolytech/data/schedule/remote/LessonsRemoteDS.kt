package com.mospolytech.data.schedule.remote

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.*
import com.mospolytech.data.schedule.model.entity.*
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import org.jetbrains.exposed.sql.*
import java.util.*

class LessonsRemoteDS {
    private val map = mutableMapOf<LessonCacheKey, String>()

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
                id = SubjectEntity.find { SubjectsDb.title eq "" }
                    .mapLazy { it.toModel() }
                    .firstOrNull()
                    ?.id

                LessonToGroupsDb.batchInsert(groupsId) {
                    this[LessonToGroupsDb.lesson] = UUID.fromString(id)
                    this[LessonToGroupsDb.group] = it
                }
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
                id = LessonEntity.new {
                    this.type = type
                    this.subject = subject
                }.toLessonModel().id

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
                    this[LessonToPlacesDb.place] = it
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