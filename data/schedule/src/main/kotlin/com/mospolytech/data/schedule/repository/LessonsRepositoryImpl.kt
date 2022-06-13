package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.data.peoples.model.entity.StudentEntity
import com.mospolytech.data.schedule.converters.ApiScheduleConverter
import com.mospolytech.data.schedule.converters.mergeLessons
import com.mospolytech.data.schedule.local.ScheduleCacheDS
import com.mospolytech.data.schedule.model.db.*
import com.mospolytech.data.schedule.model.entity.LessonEntity
import com.mospolytech.data.schedule.service.ScheduleService
import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.utils.filterByPlaces
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class LessonsRepositoryImpl(
    private val service: ScheduleService,
    private val converter: ApiScheduleConverter,
    private val cacheDS: ScheduleCacheDS
) : LessonsRepository {
    private val updateScheduleLock = Any()

    private suspend fun updateSchedule(): List<CompactLessonAndTimes> {
        val semester = service.getSchedules()
        val lessonsSemester = converter.convertToLessons(semester)

        val session = service.getSchedulesSession()
        val lessonsSession = converter.convertToLessons(session)

        val mergedLessons = mergeLessons(lessonsSemester, lessonsSession)

        synchronized(updateScheduleLock) {
            cacheDS.scheduleCache = mergedLessons
            cacheDS.scheduleCacheUpdateDateTime = LocalDateTime.now()
        }
        return mergedLessons
    }

    override suspend fun getLessons(): List<CompactLessonAndTimes> {
        return if (cacheDS.scheduleCacheUpdateDateTime.until(LocalDateTime.now(), ChronoUnit.HOURS) > 24) {
            updateSchedule()
        } else {
            cacheDS.scheduleCache
        }
    }

    override suspend fun getLessonsByPlaces(placeIds: List<String>): List<CompactLessonAndTimes> {
        return getLessons()
            .let { if (placeIds.isNotEmpty()) it.filterByPlaces(placeIds) else it }
    }

    suspend fun getLessons(filter: ScheduleComplexFilter): List<CompactLessonAndTimes> {
        return MosPolyDb.transaction {
            val notNeedFilterBySubject = filter.subjectsId.isEmpty()
            val notNeedFilterByType = filter.typesId.isEmpty()
            val notNeedFilterByTeachers = filter.teachersId.isEmpty()
            val notNeedFilterByGroups = filter.groupsId.isEmpty()
            val notNeedFilterByPlaces = filter.placesId.isEmpty()


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
                .selectAll()

//            if (!notNeedFilterByGroups) {
//                query = query.andWhere {
//
//                }
//            }
//
//            LessonEntity.find {
//                (notNeedFilterBySubject || it.lesson.subjectId in filter.subjectsId) and
//                        (notNeedFilterByType || it.lesson.typeId in filter.typesId) and
//                        (notNeedFilterByTeachers || it.lesson.teachersId.any { it in filter.teachersId }) and
//                        (notNeedFilterByGroups || it.lesson.groupsId.any { it in filter.groupsId }) and
//                        (notNeedFilterByPlaces || it.lesson.placesId.any { it in filter.placesId })
//            }

            LessonEntity.wrapRows(query)
                .map { it.toFullModel() }
        }
    }

    override suspend fun getAllLessons(): CompactSchedule {
        return MosPolyDb.transaction {
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
                .selectAll()

            val lessons = LessonEntity.wrapRows(query)
                .map { it.toFullModel() }

            error("")
        }
    }

    fun List<CompactLessonAndTimes>.filter(filter: ScheduleComplexFilter): List<CompactLessonAndTimes> {
        val notNeedFilterBySubject = filter.subjectsId.isEmpty()
        val notNeedFilterByType = filter.typesId.isEmpty()
        val notNeedFilterByTeachers = filter.teachersId.isEmpty()
        val notNeedFilterByGroups = filter.groupsId.isEmpty()
        val notNeedFilterByPlaces = filter.placesId.isEmpty()

        return this.filter {
            (notNeedFilterBySubject || it.lesson.subjectId in filter.subjectsId) &&
                    (notNeedFilterByType || it.lesson.typeId in filter.typesId) &&
                    (notNeedFilterByTeachers || it.lesson.teachersId.any { it in filter.teachersId }) &&
                    (notNeedFilterByGroups || it.lesson.groupsId.any { it in filter.groupsId }) &&
                    (notNeedFilterByPlaces || it.lesson.placesId.any { it in filter.placesId })
        }
    }
}