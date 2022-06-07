package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.peoples.model.description
import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.pack.ScheduleInfo
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceFull
import com.mospolytech.domain.schedule.model.source.ScheduleSources
import com.mospolytech.domain.schedule.repository.*
import com.mospolytech.domain.schedule.utils.*

class ScheduleRepositoryImpl(
    private val lessonsRepository: LessonsRepository,
    private val lessonSubjectsRepository: LessonSubjectsRepository,
    private val lessonTypesRepository: LessonTypesRepository,
    private val teachersRepository: TeachersRepository,
    private val groupsRepository: GroupsRepository,
    private val placesRepository: PlacesRepository
) : ScheduleRepository {
    private suspend fun getLessons(source: ScheduleSource): List<CompactLessonAndTimes> {
        val lessons = lessonsRepository.getLessons()
        return when (source.type) {
            ScheduleSources.Group -> lessons.filterByGroup(source.key)
            ScheduleSources.Teacher -> lessons.filterByTeacher(source.key)
            ScheduleSources.Student -> lessons
            ScheduleSources.Place -> lessons.filterByPlace(source.key)
            ScheduleSources.Subject -> lessons.filterBySubject(source.key)
            ScheduleSources.Complex -> lessons
        }
    }

    private suspend fun getLessons(filter: ScheduleComplexFilter): List<CompactLessonAndTimes> {
        val lessons = lessonsRepository.getLessons()

        return lessons.filter(filter)
    }

    override suspend fun getSourceList(sourceType: ScheduleSources): List<ScheduleSourceFull> {
        return when (sourceType) {
            ScheduleSources.Group -> {
                lessonsRepository.getLessons()
                    .flatMap { it.lesson.groupsId }
                    .mapNotNull { groupsRepository.get(it) }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.title, it.description, "") }
            }
            ScheduleSources.Teacher -> {
                lessonsRepository.getLessons()
                    .flatMap { it.lesson.teachersId }
                    .mapNotNull { teachersRepository.get(it) }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.name, it.description, "") }
            }
            ScheduleSources.Student -> {
                emptyList()
            }
            ScheduleSources.Place -> {
                lessonsRepository.getLessons()
                    .flatMap { it.lesson.placesId }
                    .mapNotNull { placesRepository.get(it) }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.title, "", "") }
            }
            ScheduleSources.Subject -> {
                lessonsRepository.getLessons()
                    .map { it.lesson.subjectId }
                    .mapNotNull { lessonSubjectsRepository.get(it) }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.title, "", "") }
            }
            ScheduleSources.Complex -> emptyList()
        }
    }

    override suspend fun getCompactSchedule(): CompactSchedule {
        return getSchedulePackFromLessons(lessonsRepository.getLessons())
    }

    override suspend fun getCompactSchedule(source: ScheduleSource): CompactSchedule {
        return getSchedulePackFromLessons(getLessons(source))
    }

    override suspend fun getCompactSchedule(filter: ScheduleComplexFilter): CompactSchedule {
        return getSchedulePackFromLessons(getLessons(filter))
    }

    private fun getSchedulePackFromLessons(lessons: List<CompactLessonAndTimes>): CompactSchedule {
        val typesId = lessons.asSequence().map { it.lesson.typeId }.distinct()
        val subjectsId = lessons.asSequence().map { it.lesson.subjectId }.distinct()
        val teachersId = lessons.asSequence().flatMap { it.lesson.teachersId }.distinct()
        val groupsId = lessons.asSequence().flatMap { it.lesson.groupsId }.distinct()
        val placesId = lessons.asSequence().flatMap { it.lesson.placesId }.distinct()

        return CompactSchedule(
            lessons = lessons,
            info = ScheduleInfo(
                typesInfo = typesId.mapNotNull { lessonTypesRepository.get(it) }.toList(),
                subjectsInfo = subjectsId.mapNotNull { lessonSubjectsRepository.get(it) }.toList(),
                teachersInfo = teachersId.mapNotNull { teachersRepository.get(it) }.toList(),
                groupsInfo = groupsId.mapNotNull { groupsRepository.get(it) }.toList(),
                placesInfo = placesId.mapNotNull { placesRepository.get(it) }.toList()
            )
        )
    }
}