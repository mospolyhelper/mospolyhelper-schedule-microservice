package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.data.schedule.model.db.*
import com.mospolytech.domain.peoples.model.description
import com.mospolytech.domain.peoples.repository.StudentsRepository
import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.lesson_subject.description
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.place.description
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceFull
import com.mospolytech.domain.schedule.model.source.ScheduleSources
import com.mospolytech.domain.schedule.repository.*
import com.mospolytech.domain.schedule.utils.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select

class ScheduleRepositoryImpl(
    private val lessonsRepository: LessonsRepository,
    private val lessonSubjectsRepository: LessonSubjectsRepository,
    private val lessonTypesRepository: LessonTypesRepository,
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

    override suspend fun getSourceList(sourceType: ScheduleSources): List<ScheduleSourceFull> {
        return when (sourceType) {
            ScheduleSources.Group -> {
                groupsRepository.getAll()
                    .map { ScheduleSourceFull(sourceType, it.id, it.title, it.description, null) }
            }
            ScheduleSources.Teacher -> {
                teachersRepository.getAll()
                    .map { ScheduleSourceFull(sourceType, it.id, it.name, it.description, it.avatar) }
            }
            ScheduleSources.Student -> {
                studentsRepository.getShortStudents()
                    .map { ScheduleSourceFull(sourceType, it.id, it.name, it.description, it.avatar) }
            }
            ScheduleSources.Place -> {
                placesRepository.getAll()
                    .map { ScheduleSourceFull(sourceType, it.id, it.title, it.description, null) }
            }
            ScheduleSources.Subject -> {
                lessonSubjectsRepository.getAll()
                    .map { ScheduleSourceFull(sourceType, it.id, it.title, it.description, null) }
            }
            ScheduleSources.Complex -> error("Can't process ScheduleSources.Complex")
        }
    }

    override suspend fun getCompactSchedule(): CompactSchedule {
        return lessonsRepository.getAllLessons()
    }

    override suspend fun getCompactSchedule(source: ScheduleSource): CompactSchedule {
        return when (source.type) {
            ScheduleSources.Group -> lessonsRepository.getLessonsByGroup(source.key)
            ScheduleSources.Teacher -> lessonsRepository.getLessonsByTeacher(source.key)
            ScheduleSources.Student -> lessonsRepository.getLessonsByStudent(source.key)
            ScheduleSources.Place -> lessonsRepository.getLessonsByPlace(source.key)
            ScheduleSources.Subject -> lessonsRepository.getLessonsBySubject(source.key)
            ScheduleSources.Complex -> error("Can't process ScheduleSources.Complex")
        }
    }

    override suspend fun findGroupByTitle(title: String): String? {
        return MosPolyDb.transaction {
            GroupsDb.select {
                GroupsDb.title eq title
            }.firstOrNull()?.let {
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

                    LessonToLessonDateTimesDb,
                    LessonToTeachersDb,
                    LessonToGroupsDb,
                    LessonToPlacesDb,

                    LessonTypesDb,
                    SubjectsDb,
                    LessonDateTimesDb,
                    PlacesDb,
                )
            }
            MosPolyDb.transaction {
                SchemaUtils.create(
                    LessonsDb,

                    LessonToLessonDateTimesDb,
                    LessonToTeachersDb,
                    LessonToGroupsDb,
                    LessonToPlacesDb,

                    LessonTypesDb,
                    SubjectsDb,
                    LessonDateTimesDb,
                    PlacesDb,
                )
            }
        } else {
//            MosPolyDb.transaction {
//                    LessonDateTimesDb.deleteAll()
//                    LessonsDb.deleteAll()
//                    LessonToGroupsDb.deleteAll()
//                    LessonToLessonDateTimesDb.deleteAll()
//                    LessonToPlacesDb.deleteAll()
//                    LessonToTeachersDb.deleteAll()
//                    LessonTypesDb.deleteAll()
//                    PlacesDb.deleteAll()
//                    StudentsDb.deleteAll()
//            }
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
