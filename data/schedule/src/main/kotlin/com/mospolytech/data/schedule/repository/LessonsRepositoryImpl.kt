package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.entity.GroupEntity
import com.mospolytech.data.peoples.model.entity.StudentEntity
import com.mospolytech.data.peoples.model.entity.TeacherEntity
import com.mospolytech.data.peoples.model.entity.description
import com.mospolytech.data.schedule.converters.ApiScheduleConverter
import com.mospolytech.data.schedule.model.db.*
import com.mospolytech.data.schedule.model.entity.LessonEntity
import com.mospolytech.data.schedule.model.entity.SubjectEntity
import com.mospolytech.data.schedule.service.ScheduleService
import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.lessonSubject.CompactLessonSubjectInfo
import com.mospolytech.domain.schedule.model.pack.AttendeeInfo
import com.mospolytech.domain.schedule.model.pack.AttendeeType
import com.mospolytech.domain.schedule.model.pack.CompactLessonEvent
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.place.CompactPlaceInfo
import com.mospolytech.domain.schedule.repository.LessonsRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import java.util.UUID

class LessonsRepositoryImpl(
    private val service: ScheduleService,
    private val converter: ApiScheduleConverter,
) : LessonsRepository {
    override suspend fun updateSchedule() {
        val semester = service.getSchedules()
        converter.convertToLessons(semester)

        val session = service.getSchedulesSession()
        converter.convertToLessons(session)

        converter.pushALlLessons()
        converter.clearCache()
    }

    override suspend fun getLessonsByPlaces(placeIds: List<String>): List<CompactLessonEvent> {
        return MosPolyDb.transaction {
            val resLessons =
                if (placeIds.isNotEmpty()) {
                    val placesId = placeIds.map { UUID.fromString(it) }

                    LessonToPlacesDb.selectAll()
                        .where { LessonToPlacesDb.place inList placesId }
                        .map { it[LessonToPlacesDb.lesson].value }.toSet()
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
            val query =
                fullQuery()
                    .selectAll()

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsByGroup(groupId: String): CompactSchedule {
        return MosPolyDb.transaction {
            val lessonsId =
                LessonToGroupsDb.selectAll()
                    .where { LessonToGroupsDb.group eq groupId }
                    .mapLazy { it[LessonToGroupsDb.lesson].value }
                    .toList()

            val query =
                fullQuery().selectAll()
                    .where { LessonsDb.id inList lessonsId }

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsByStudent(studentId: String): CompactSchedule {
        return MosPolyDb.transaction {
            val groupId = StudentEntity.findById(studentId)?.group?.id?.value.orEmpty()

            val lessonsId =
                LessonToGroupsDb.selectAll()
                    .where { LessonToGroupsDb.group eq groupId }
                    .mapLazy { it[LessonToGroupsDb.lesson].value }
                    .toList()

            val query =
                fullQuery().selectAll()
                    .where { LessonsDb.id inList lessonsId }

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsByTeacher(teacherId: String): CompactSchedule {
        return MosPolyDb.transaction {
            val lessonsId =
                LessonToTeachersDb.selectAll()
                    .where { LessonToTeachersDb.teacher eq teacherId }
                    .mapLazy { it[LessonToTeachersDb.lesson].value }
                    .toList()

            val query =
                fullQuery().selectAll()
                    .where { LessonsDb.id inList lessonsId }

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsByPlace(placeId: String): CompactSchedule {
        return MosPolyDb.transaction {
            val lessonsId =
                LessonToPlacesDb.selectAll()
                    .where { LessonToPlacesDb.place eq UUID.fromString(placeId) }
                    .mapLazy { it[LessonToPlacesDb.lesson].value }
                    .toList()

            val query =
                fullQuery().selectAll()
                    .where { LessonsDb.id inList lessonsId }

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsBySubject(subjectId: String): CompactSchedule {
        return MosPolyDb.transaction {
            val query =
                fullQuery().selectAll()
                    .where { LessonsDb.subject eq subjectId }

            buildSchedule(query)
        }
    }

    override suspend fun getLessonsByFilter(filter: ScheduleComplexFilter): CompactSchedule {
        return MosPolyDb.transaction {
            var isInitialized = false
            var resLessons = setOf<UUID>()

            fun updateResList(lessons: Set<UUID>) {
                resLessons =
                    if (isInitialized) {
                        resLessons intersect lessons
                    } else {
                        lessons
                    }
                isInitialized = true
            }

            if (filter.groupsId.isNotEmpty()) {
                val lessonsToIntersect =
                    LessonToGroupsDb.selectAll()
                        .where { LessonToGroupsDb.group inList filter.groupsId }
                        .map { it[LessonToGroupsDb.lesson].value }
                        .toSet()

                updateResList(lessonsToIntersect)
                if (isInitialized && resLessons.isEmpty()) return@transaction CompactSchedule.empty
            }

            if (filter.teachersId.isNotEmpty()) {
                val lessonsToIntersect =
                    LessonToTeachersDb.selectAll()
                        .where { LessonToTeachersDb.teacher inList filter.teachersId }
                        .map { it[LessonToTeachersDb.lesson].value }
                        .toSet()

                updateResList(lessonsToIntersect)
                if (isInitialized && resLessons.isEmpty()) return@transaction CompactSchedule.empty
            }

            if (filter.placesId.isNotEmpty()) {
                val placesId = filter.placesId.map { UUID.fromString(it) }
                val lessonsToIntersect =
                    LessonToPlacesDb.selectAll()
                        .where { LessonToPlacesDb.place inList placesId }
                        .map { it[LessonToPlacesDb.lesson].value }
                        .toSet()

                updateResList(lessonsToIntersect)
                if (isInitialized && resLessons.isEmpty()) return@transaction CompactSchedule.empty
            }

            val query = fullQuery().selectAll()

            if (resLessons.isNotEmpty()) {
                query.andWhere { LessonsDb.id inList resLessons }
            }

            if (filter.subjectsId.isNotEmpty()) {
                val subjectsId = filter.subjectsId
                query.andWhere {
                    LessonsDb.subject inList subjectsId
                }
            }

            if (filter.typesId.isNotEmpty()) {
                val typesId = filter.typesId
                query.andWhere {
                    LessonsDb.type inList typesId
                }
            }

            buildSchedule(query)
        }
    }

    private fun fullQuery(): ColumnSet {
        val query = LessonsDb
//            .leftJoin(LessonTypesDb)
//            .leftJoin(SubjectsDb)
//            .leftJoin(LessonToTeachersDb)
//            .leftJoin(
//                TeachersDb.leftJoin(DepartmentsDb, { department }, { id })
//            )
//            .leftJoin(LessonToGroupsDb)
//            .leftJoin(
//                GroupsDb.leftJoin(StudentFacultiesDb)
//                    .leftJoin(StudentDirectionsDb)
//            )
//            .leftJoin(LessonToPlacesDb)
//            .leftJoin(
//                PlacesDb
//            )
//            .leftJoin(LessonToLessonDateTimesDb)
//            .leftJoin(
//                LessonDateTimesDb
//            )

        return query
    }

    private fun buildSchedule(query: Query): CompactSchedule {
        val subjects: MutableCollection<CompactLessonSubjectInfo> = LinkedHashSet()
        val attendees: MutableCollection<AttendeeInfo> = LinkedHashSet()
        val places: MutableCollection<CompactPlaceInfo> = LinkedHashSet()
        val lessonsList = mutableListOf<CompactLessonEvent>()

        LessonEntity.wrapRows(query).forEach {
            subjects.add(it.subject.toLessonSubjectInfo())
            attendees.addAll(it.teachers.map { it.toAttendee() })
            attendees.addAll(it.groups.map { it.toAttendee() })
            places.addAll(it.places.map { it.toCompactModel() })
            lessonsList.add(it.toModel())
        }

        return CompactSchedule(
            lessons = lessonsList,
            subjects = subjects.toList(),
            attendees = attendees.toList(),
            places = places.toList(),
        )
    }
}

fun TeacherEntity.toAttendee(): AttendeeInfo {
    val type = AttendeeType.Teacher
    return AttendeeInfo(
        id = type.createFullId(this.id.toString()),
        type = type,
        name = this.name,
        description = this.description,
        avatar = this.avatar,
    )
}

fun GroupEntity.toAttendee(): AttendeeInfo {
    val type = AttendeeType.Group
    return AttendeeInfo(
        id = type.createFullId(this.id.toString()),
        type = type,
        name = this.title,
        description = this.description,
        avatar = null,
    )
}

fun SubjectEntity.toLessonSubjectInfo(): CompactLessonSubjectInfo {
    return CompactLessonSubjectInfo(
        id = id.value.toString(),
        title = title,
        type = type,
        description = description,
    )
}
