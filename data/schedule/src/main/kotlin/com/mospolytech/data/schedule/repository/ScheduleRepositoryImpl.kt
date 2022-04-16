package com.mospolytech.data.schedule.repository

import com.mospolytech.data.schedule.converters.buildSchedule
import com.mospolytech.data.schedule.converters.getDateRange
import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import com.mospolytech.domain.schedule.model.lesson.LessonTime
import com.mospolytech.domain.schedule.model.lesson_type.LessonType
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.pack.CompactLessonFeatures
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.pack.ScheduleInfo
import com.mospolytech.domain.schedule.model.review.LessonReviewDay
import com.mospolytech.domain.schedule.model.review.LessonTimesReview
import com.mospolytech.domain.schedule.model.review.LessonTimesReviewByType
import com.mospolytech.domain.schedule.model.schedule.ScheduleDay
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceFull
import com.mospolytech.domain.schedule.model.source.ScheduleSources
import com.mospolytech.domain.schedule.repository.*
import com.mospolytech.domain.schedule.utils.filterByGroup
import com.mospolytech.domain.schedule.utils.filterByPlace
import com.mospolytech.domain.schedule.utils.filterBySubject
import com.mospolytech.domain.schedule.utils.filterByTeacher
import java.time.DayOfWeek
import java.time.LocalDate

class ScheduleRepositoryImpl(
    private val lessonsRepository: LessonsRepository,
    private val lessonSubjectsRepository: LessonSubjectsRepository,
    private val lessonTypesRepository: LessonTypesRepository,
    private val teachersRepository: TeachersRepository,
    private val groupsRepository: GroupsRepository,
    private val placesRepository: PlacesRepository
) : ScheduleRepository {

    private fun getLessonDateRange(lessons: List<LessonDateTimes>): Pair<LocalDate, LocalDate> {
        var minDate = LocalDate.MAX
        var maxDate = LocalDate.MIN

        for (lessonDateTimes in lessons) {
            for (dateTime in lessonDateTimes.time) {
                if (dateTime.startDate < minDate) {
                    minDate = dateTime.startDate
                }

                if (dateTime.startDate > maxDate) {
                    maxDate = dateTime.startDate
                }
            }
        }

        if (minDate == LocalDate.MAX && maxDate == LocalDate.MIN) {
            minDate = LocalDate.now()
            maxDate = LocalDate.now()
        }

        return minDate to maxDate
    }

    override suspend fun getSchedule(): List<ScheduleDay> {
        val lessons = lessonsRepository.getLessons()
        val (minDate, maxDate) = getLessonDateRange(lessons)

        return buildSchedule(lessons, minDate, maxDate)
    }

    override suspend fun getSchedule(source: ScheduleSource): List<ScheduleDay> {
        val lessons = getLessons(source)
        val (minDate, maxDate) = getLessonDateRange(lessons)
        return buildSchedule(lessons, minDate, maxDate)
    }


    private suspend fun getLessons(source: ScheduleSource): List<LessonDateTimes> {
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

    override suspend fun getSourceList(sourceType: ScheduleSources): List<ScheduleSourceFull> {
        return when (sourceType) {
            ScheduleSources.Group -> {
                lessonsRepository.getLessons()
                    .flatMap { it.lesson.groups }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.title, "", "") }
            }
            ScheduleSources.Teacher -> {
                lessonsRepository.getLessons()
                    .flatMap { it.lesson.teachers }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.name, "", "") }
            }
            ScheduleSources.Student -> {
                lessonsRepository.getLessons()
                    .flatMap { it.lesson.teachers }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.name, "", "") }
            }
            ScheduleSources.Place -> {
                lessonsRepository.getLessons()
                    .flatMap { it.lesson.places }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.title, "", "") }
            }
            ScheduleSources.Subject -> {
                lessonsRepository.getLessons()
                    .map { it.lesson.subject }
                    .toSortedSet()
                    .map { ScheduleSourceFull(sourceType, it.id, it.title, "", "") }
            }
            ScheduleSources.Complex -> emptyList()
        }
    }

    override suspend fun getLessonsReview(source: ScheduleSource): List<LessonTimesReview> {
        val lessons = getLessons(source)

        val resMap = mutableMapOf<String, MutableMap<LessonType, MutableMap<DayReviewUnit, MutableList<LocalDate>>>>()


        lessons.forEach { lessonDateTimes ->
            lessonDateTimes.time.forEach { lessonDateTime ->
                val key = DayReviewUnit(
                    dayOfWeek = lessonDateTime.startDate.dayOfWeek,
                    lessonTime = lessonDateTime.time
                )
                resMap
                    .getOrPut(lessonDateTimes.lesson.subject.title) { mutableMapOf() }
                    .getOrPut(lessonDateTimes.lesson.type) { mutableMapOf() }
                    .getOrPut(key) { mutableListOf() }
                    .add(lessonDateTime.startDate)
            }
        }


        val resList = resMap.map { (title, typeToDays) ->
            LessonTimesReview(
                lessonTitle = title,
                days = typeToDays.map { (type, mapOfDays) ->
                    LessonTimesReviewByType(lessonType = type,
                       days = mapOfDays.map { (dayReviewUnit, dateList) ->
                           val (dateFrom, dateTo) = getDateRange(dateList)

                           LessonReviewDay(
                               dayOfWeek = dayReviewUnit.dayOfWeek,
                               time = dayReviewUnit.lessonTime,
                               dateFrom = dateFrom,
                               dateTo = dateTo
                           )
                       }.sorted()
                    )
                }.sorted()
            )
        }.sortedBy { it.lessonTitle }

        return resList
    }

    override suspend fun getSchedulePack(source: ScheduleSource): CompactSchedule {
        return getSchedulePackFromLessons(getLessons(source))
    }

    override suspend fun getSchedulePack(): CompactSchedule {
        return getSchedulePackFromLessons(lessonsRepository.getLessons())
    }

    private fun getSchedulePackFromLessons(lessons: List<LessonDateTimes>): CompactSchedule {
        val lessons = lessons.map {
            CompactLessonAndTimes(
                lesson = CompactLessonFeatures(
                    typeId = it.lesson.type.id,
                    subjectId = it.lesson.subject.id,
                    teachersId = it.lesson.teachers.map { it.id },
                    groupsId = it.lesson.groups.map { it.id },
                    placesId = it.lesson.places.map { it.id }
                ),
                times = it.time
            )
        }

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

    data class DayReviewUnit(
        val dayOfWeek: DayOfWeek,
        val lessonTime: LessonTime
    )
}