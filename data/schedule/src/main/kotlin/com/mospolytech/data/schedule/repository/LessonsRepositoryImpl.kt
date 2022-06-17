package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.data.peoples.model.entity.StudentEntity
import com.mospolytech.data.schedule.converters.ApiScheduleConverter
import com.mospolytech.data.schedule.local.ScheduleCacheDS
import com.mospolytech.data.schedule.model.db.*
import com.mospolytech.data.schedule.model.entity.LessonEntity
import com.mospolytech.data.schedule.service.ScheduleService
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubjectInfo
import com.mospolytech.domain.schedule.model.lesson_type.LessonTypeInfo
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.pack.ScheduleInfo
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.utils.filterByPlaces
import org.jetbrains.exposed.sql.*
import java.util.UUID

class LessonsRepositoryImpl(
    private val service: ScheduleService,
    private val converter: ApiScheduleConverter
) : LessonsRepository {
    override suspend fun updateSchedule() {
        val semester = service.getSchedules()
        converter.convertToLessons(semester)

        val session = service.getSchedulesSession()
        converter.convertToLessons(session)
    }

    override suspend fun getLessonsByPlaces(placeIds: List<String>): List<CompactLessonAndTimes> {
        return MosPolyDb.transaction {
            val resLessons = if (placeIds.isNotEmpty()) {
                val placesId = placeIds.map { UUID.fromString(it) }

                LessonToPlacesDb.select {
                    LessonToPlacesDb.place inList placesId
                }.map { it[LessonToPlacesDb.lesson].value }.toSet()
            } else {
                setOf<UUID>()
            }

            val query = fullQuery().selectAll()
            if (resLessons.isNotEmpty()) {
                query.andWhere { LessonsDb.id inList resLessons }
            }

            buildSchedule(query).lessons
        }
    }

    override suspend fun getAllLessons(): CompactSchedule {
        return MosPolyDb.transaction {
            val query = fullQuery()
                .selectAll()

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsByGroup(groupId: String): CompactSchedule {
        return MosPolyDb.transaction {
            val lessonsId = LessonToGroupsDb.select {
                LessonToGroupsDb.group eq groupId
            }.mapLazy { it[LessonToGroupsDb.lesson].value }
                .toList()

            val query = fullQuery().select {
                LessonsDb.id inList lessonsId
            }

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsByStudent(studentId: String): CompactSchedule {
        return MosPolyDb.transaction {
            val groupId = StudentEntity.findById(studentId)?.group?.id?.value.orEmpty()

            val lessonsId = LessonToGroupsDb.select {
                LessonToGroupsDb.group eq groupId
            }.mapLazy { it[LessonToGroupsDb.lesson].value }
                .toList()

            val query = fullQuery().select {
                LessonsDb.id inList lessonsId
            }

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsByTeacher(teacherId: String): CompactSchedule {
        return MosPolyDb.transaction {
            val lessonsId = LessonToTeachersDb.select {
                LessonToTeachersDb.teacher eq teacherId
            }.mapLazy { it[LessonToGroupsDb.lesson].value }
                .toList()

            val query = fullQuery().select {
                LessonsDb.id inList lessonsId
            }

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsByPlace(placeId: String): CompactSchedule {
        return MosPolyDb.transaction {
            val lessonsId = LessonToPlacesDb.select {
                LessonToPlacesDb.place eq UUID.fromString(placeId)
            }.mapLazy { it[LessonToGroupsDb.lesson].value }
                .toList()

            val query = fullQuery().select {
                LessonsDb.id inList lessonsId
            }

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsBySubject(subjectId: String): CompactSchedule {
        return MosPolyDb.transaction {
            val query = fullQuery().select {
                LessonsDb.subject eq UUID.fromString(subjectId)
            }

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsByFilter(filter: ScheduleComplexFilter): CompactSchedule {
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

            if (filter.groupsId.isNotEmpty()) {
                val lessonsToIntersect = LessonToGroupsDb.select {
                    LessonToGroupsDb.group inList filter.groupsId
                }.map { it[LessonToGroupsDb.lesson].value }.toSet()

                updateResList(lessonsToIntersect)
                if (isInitialized && resLessons.isEmpty()) return@transaction CompactSchedule.empty
            }

            if (filter.teachersId.isNotEmpty()) {
                val lessonsToIntersect = LessonToTeachersDb.select {
                    LessonToTeachersDb.teacher inList filter.teachersId
                }.map { it[LessonToTeachersDb.lesson].value }.toSet()

                updateResList(lessonsToIntersect)
                if (isInitialized && resLessons.isEmpty()) return@transaction CompactSchedule.empty
            }

            if (filter.placesId.isNotEmpty()) {
                val placesId = filter.placesId.map { UUID.fromString(it) }
                val lessonsToIntersect = LessonToPlacesDb.select {
                    LessonToPlacesDb.place inList placesId
                }.map { it[LessonToPlacesDb.lesson].value }.toSet()

                updateResList(lessonsToIntersect)
                if (isInitialized && resLessons.isEmpty()) return@transaction CompactSchedule.empty
            }

            val query = fullQuery().selectAll()

            if (resLessons.isNotEmpty()) {
                query.andWhere { LessonsDb.id inList resLessons }
            }

            if (filter.subjectsId.isNotEmpty()) {
                val subjectsId = filter.subjectsId.map { UUID.fromString(it) }
                query.andWhere {
                    LessonsDb.subject inList subjectsId
                }
            }

            if (filter.typesId.isNotEmpty()) {
                val typesId = filter.typesId.map { UUID.fromString(it) }
                query.andWhere {
                    LessonsDb.type inList typesId
                }
            }

            buildSchedule(query)
        }
    }

    private fun fullQuery(): ColumnSet {
        val query = LessonsDb
            .innerJoin(LessonTypesDb)
            .innerJoin(SubjectsDb)
            .innerJoin(LessonToTeachersDb)
            .innerJoin(
                TeachersDb.leftJoin(DepartmentsDb)
            )
            .innerJoin(LessonToGroupsDb)
            .innerJoin(
                GroupsDb.leftJoin(StudentFacultiesDb)
                    .leftJoin(StudentDirectionsDb)
            )
            .innerJoin(LessonToPlacesDb)
            .innerJoin(
                PlacesDb
            )
            .innerJoin(LessonToPlacesDb)
            .innerJoin(
                PlacesDb
            )

        return query
    }

    private fun buildSchedule(query: Query): CompactSchedule {
        val types = mutableListOf<LessonTypeInfo>()
        val subjects = mutableListOf<LessonSubjectInfo>()
        val teachers = mutableListOf<Teacher>()
        val groups = mutableListOf<Group>()
        val places = mutableListOf<PlaceInfo>()
        val lessonsList = mutableListOf<CompactLessonAndTimes>()


        LessonEntity.wrapRows(query).forEach {
            types.add(it.type.toModel())
            subjects.add(it.subject.toModel())
            teachers.addAll(it.teachers.map { it.toModel() })
            groups.addAll(it.groups.map { it.toModel() })
            places.addAll(it.places.map { it.toModel() })
            lessonsList.add(it.toFullModel())
        }

        return CompactSchedule(
            lessons = lessonsList,
            info = ScheduleInfo(
                typesInfo = types,
                subjectsInfo = subjects,
                teachersInfo = teachers,
                groupsInfo = groups,
                placesInfo = places
            )
        )
    }
}