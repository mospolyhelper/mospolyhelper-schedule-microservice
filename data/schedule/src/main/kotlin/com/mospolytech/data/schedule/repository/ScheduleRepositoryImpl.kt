package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.GroupsDb
import com.mospolytech.data.schedule.model.db.LessonToGroupsDb
import com.mospolytech.data.schedule.model.db.LessonToPlacesDb
import com.mospolytech.data.schedule.model.db.LessonToTeachersDb
import com.mospolytech.data.schedule.model.db.LessonsDb
import com.mospolytech.data.schedule.model.db.PlacesDb
import com.mospolytech.data.schedule.model.db.RecurrenceDb
import com.mospolytech.data.schedule.model.db.SubjectsDb
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.base.utils.map
import com.mospolytech.domain.peoples.repository.StudentsRepository
import com.mospolytech.domain.peoples.repository.TeachersRepository
import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceTypes
import com.mospolytech.domain.schedule.repository.GroupsRepository
import com.mospolytech.domain.schedule.repository.LessonSubjectsRepository
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.repository.PlacesRepository
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.selectAll

class ScheduleRepositoryImpl(
    private val lessonsRepository: LessonsRepository,
    private val lessonSubjectsRepository: LessonSubjectsRepository,
    private val teachersRepository: TeachersRepository,
    private val groupsRepository: GroupsRepository,
    private val placesRepository: PlacesRepository,
    private val studentsRepository: StudentsRepository,
) : ScheduleRepository {
//    private suspend fun getLessons(source: ScheduleSource): List<CompactLessonAndTimes> {
//        val lessons = lessonsRepository.getLessons()
//        return when (source.type) {
//            ScheduleSources.Group -> lessons.filterByGroup(source.key)
//            ScheduleSources.Teacher -> lessons.filterByTeacher(source.key)
//            ScheduleSources.Student -> lessons.filterByGroup(source.key)
//            ScheduleSources.Place -> lessons.filterByPlace(source.key)
//            ScheduleSources.Subject -> lessons.filterBySubject(source.key)
//            ScheduleSources.Complex -> lessons
//        }
//    }
//
//    private suspend fun getLessons(filter: ScheduleComplexFilter): List<CompactLessonAndTimes> {
//        val lessons = lessonsRepository.getLessons()
//
//        return lessons.filter(filter)
//    }

    override suspend fun getSourceList(
        sourceType: ScheduleSourceTypes,
        query: String,
        page: Int,
        limit: Int,
    ): PagingDTO<ScheduleSource> {
        return when (sourceType) {
            ScheduleSourceTypes.Group -> {
                groupsRepository.getPagingShort(
                    query = query,
                    page = page,
                    pageSize = limit,
                ).map {
                    ScheduleSource(
                        type = sourceType,
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        avatar = null,
                    )
                }
            }
            ScheduleSourceTypes.Teacher -> {
                teachersRepository.getTeachers(
                    query = query,
                    page = page,
                    limit = limit,
                ).map {
                    ScheduleSource(
                        type = sourceType,
                        id = it.id,
                        title = it.name,
                        description = it.description,
                        avatar = it.avatar,
                    )
                }
            }
            ScheduleSourceTypes.Student -> {
                studentsRepository.getShortStudents(
                    query = query,
                    page = page,
                    limit = limit,
                ).map {
                    ScheduleSource(
                        type = sourceType,
                        id = it.id,
                        title = it.name,
                        description = it.description,
                        avatar = it.avatar,
                    )
                }
            }
            ScheduleSourceTypes.Place -> {
                placesRepository.getPaging(
                    query = query,
                    page = page,
                    pageSize = limit,
                ).map {
                    ScheduleSource(
                        type = sourceType,
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        avatar = null,
                    )
                }
            }
            ScheduleSourceTypes.Subject -> {
                lessonSubjectsRepository.getPaging(
                    query = query,
                    page = page,
                    pageSize = limit,
                ).map {
                    ScheduleSource(
                        type = sourceType,
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        avatar = null,
                    )
                }
            }
            ScheduleSourceTypes.Complex -> error("Can't process ScheduleSources.Complex")
        }
    }

    override suspend fun getCompactSchedule(): CompactSchedule {
        return lessonsRepository.getAllLessons()
    }

    override suspend fun getCompactSchedule(
        id: String,
        type: ScheduleSourceTypes,
    ): CompactSchedule {
        return when (type) {
            ScheduleSourceTypes.Group -> lessonsRepository.getLessonsByGroup(id)
            ScheduleSourceTypes.Teacher -> lessonsRepository.getLessonsByTeacher(id)
            ScheduleSourceTypes.Student -> lessonsRepository.getLessonsByStudent(id)
            ScheduleSourceTypes.Place -> lessonsRepository.getLessonsByPlace(id)
            ScheduleSourceTypes.Subject -> lessonsRepository.getLessonsBySubject(id)
            ScheduleSourceTypes.Complex -> error("Can't process ScheduleSources.Complex")
        }
    }

    override suspend fun findGroupByTitle(title: String): String? {
        return MosPolyDb.transaction {
            GroupsDb.selectAll()
                .where { GroupsDb.title eq title }
                .firstOrNull()?.let {
                    it[GroupsDb.id].value
                }
        }
    }

    override suspend fun getCompactSchedule(filter: ScheduleComplexFilter): CompactSchedule {
        return lessonsRepository.getLessonsByFilter(filter)
    }

    override suspend fun updateData(recreateDb: Boolean) {
        if (recreateDb) {
            MosPolyDb.transaction {
                SchemaUtils.drop(
                    LessonsDb,
                    RecurrenceDb,
                    LessonToTeachersDb,
                    LessonToGroupsDb,
                    LessonToPlacesDb,
                    SubjectsDb,
                    PlacesDb,
                )
            }
            MosPolyDb.transaction {
                SchemaUtils.create(
                    SubjectsDb,
                    PlacesDb,
                    RecurrenceDb,
                    LessonsDb,
                    LessonToTeachersDb,
                    LessonToGroupsDb,
                    LessonToPlacesDb,
                )
            }
        } else {
            MosPolyDb.transaction {
                SchemaUtils.createMissingTablesAndColumns(
                    SubjectsDb,
                    PlacesDb,
                    RecurrenceDb,
                    LessonsDb,
                    LessonToTeachersDb,
                    LessonToGroupsDb,
                    LessonToPlacesDb,
                )
            }

            MosPolyDb.transaction {
                // Сперва очищаем таблицы с перекрёстными ссылками
                RecurrenceDb.deleteAll()
                LessonToGroupsDb.deleteAll()
                LessonToPlacesDb.deleteAll()
                LessonToTeachersDb.deleteAll()

//                LessonTypesDb.deleteAll()
//                SubjectsDb.deleteAll()
//                LessonDateTimesDb.deleteAll()
//                PlacesDb.deleteAll()

                LessonsDb.deleteAll()
            }
        }
        addSchedule()
    }

    private suspend fun addSchedule() {
        lessonsRepository.updateSchedule()
    }

//    private fun getSchedulePackFromLessons(lessons: List<CompactLessonAndTimes>): CompactSchedule {
//        val typesId = lessons.asSequence().map { it.lesson.typeId }.distinct()
//        val subjectsId = lessons.asSequence().map { it.lesson.subjectId }.distinct()
//        val teachersId = lessons.asSequence().flatMap { it.lesson.teachersId }.distinct()
//        val groupsId = lessons.asSequence().flatMap { it.lesson.groupsId }.distinct()
//        val placesId = lessons.asSequence().flatMap { it.lesson.placesId }.distinct()
//
//        return CompactSchedule(
//            lessons = lessons,
//            info = ScheduleInfo(
//                typesInfo = typesId.mapNotNull { lessonTypesRepository.get(it) }.toList(),
//                subjectsInfo = subjectsId.mapNotNull { lessonSubjectsRepository.get(it) }.toList(),
//                teachersInfo = teachersId.mapNotNull { teachersRepository.get(it) }.toList(),
//                groupsInfo = groupsId.mapNotNull { groupsRepository.get(it) }.toList(),
//                placesInfo = placesId.mapNotNull { placesRepository.get(it) }.toList()
//            )
//        )
//    }
}
