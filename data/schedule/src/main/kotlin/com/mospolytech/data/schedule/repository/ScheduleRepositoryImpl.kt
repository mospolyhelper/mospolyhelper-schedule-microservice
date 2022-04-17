package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.lesson.Lesson
import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import com.mospolytech.domain.schedule.model.lesson.LessonTime
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.pack.CompactSchedule
import com.mospolytech.domain.schedule.model.pack.ScheduleInfo
import com.mospolytech.domain.schedule.model.review.LessonReviewDay
import com.mospolytech.domain.schedule.model.review.LessonTimesReview
import com.mospolytech.domain.schedule.model.review.LessonTimesReviewByType
import com.mospolytech.domain.schedule.model.schedule.LessonsByTime
import com.mospolytech.domain.schedule.model.schedule.ScheduleDay
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSourceFull
import com.mospolytech.domain.schedule.model.source.ScheduleSources
import com.mospolytech.domain.schedule.repository.*
import com.mospolytech.domain.schedule.utils.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

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

//    suspend fun getSchedule(): List<ScheduleDay> {
//        val lessons = lessonsRepository.getLessons()
//        val (minDate, maxDate) = getLessonDateRange(lessons)
//
//        return buildSchedule(lessons, minDate, maxDate)
//    }
//
//    suspend fun getSchedule(source: ScheduleSource): List<ScheduleDay> {
//        val lessons = getLessons(source)
//        val (minDate, maxDate) = getLessonDateRange(lessons)
//        return buildSchedule(lessons, minDate, maxDate)
//    }


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
                    .map { ScheduleSourceFull(sourceType, it.id, it.name, "", "") }
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

    override suspend fun getLessonsReview(source: ScheduleSource): List<LessonTimesReview> {
        val lessons = getLessons(source)

        val resMap = mutableMapOf<String, MutableMap<String, MutableMap<DayReviewUnit, MutableList<LocalDate>>>>()


        lessons.forEach { lessonDateTimes ->
            lessonDateTimes.times.forEach { lessonDateTime ->
                val key = DayReviewUnit(
                    dayOfWeek = lessonDateTime.startDate.dayOfWeek,
                    lessonTime = lessonDateTime.time
                )
                resMap
                    .getOrPut(lessonDateTimes.lesson.subjectId) { mutableMapOf() }
                    .getOrPut(lessonDateTimes.lesson.typeId) { mutableMapOf() }
                    .getOrPut(key) { mutableListOf() }
                    .add(lessonDateTime.startDate)
            }
        }


        val resList = resMap.map { (title, typeToDays) ->
            LessonTimesReview(
                lessonTitle = title.let { lessonSubjectsRepository.get(it) }?.title ?: "",
                days = typeToDays.map { (type, mapOfDays) ->
                    LessonTimesReviewByType(lessonType = type.let { lessonTypesRepository.get(it) }?.title ?: "",
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

    data class DayReviewUnit(
        val dayOfWeek: DayOfWeek,
        val lessonTime: LessonTime
    )


    fun buildSchedule(
        lessons: List<LessonDateTimes>,
        dateFrom: LocalDate,
        dateTo: LocalDate
    ): List<ScheduleDay> {
        val resMap: MutableMap<LocalDate, MutableMap<LessonTime, MutableList<Lesson>>> = TreeMap()

        var currentDay = dateFrom
        do {
            resMap[currentDay] = TreeMap<LessonTime, MutableList<Lesson>>()
            currentDay = currentDay.plusDays(1)
        } while (currentDay <= dateTo)

        for (lessonDateTimes in lessons) {
            for (dateTime in lessonDateTimes.time) {
                val timeToLessonsMap = resMap.getOrPut(dateTime.startDate) { TreeMap<LessonTime, MutableList<Lesson>>() }
                val lessonList = timeToLessonsMap.getOrPut(dateTime.time) { mutableListOf() }
                lessonList.add(lessonDateTimes.lesson)
            }
        }

        val lessons = resMap.map { (key, value) ->
            val l1 = value.map { (key2, value2) ->
                LessonsByTime(
                    time = key2,
                    value2
                )
            }

            ScheduleDay(
                date = key,
                lessons = l1
            )
        }

        return lessons
    }

    fun getDateRange(lessons: List<LocalDate>): Pair<LocalDate, LocalDate> {
        var minDate = LocalDate.MAX
        var maxDate = LocalDate.MIN

        for (dateTime in lessons) {
            if (dateTime < minDate) {
                minDate = dateTime
            }

            if (dateTime > maxDate) {
                maxDate = dateTime
            }
        }

        return minDate to maxDate
    }
}