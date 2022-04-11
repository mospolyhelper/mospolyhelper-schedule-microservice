package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.ApiGroup
import com.mospolytech.data.schedule.model.ApiLesson
import com.mospolytech.data.schedule.model.ScheduleResponse
import com.mospolytech.data.schedule.model.ScheduleSessionResponse
import com.mospolytech.domain.schedule.model.group.Group
import com.mospolytech.domain.schedule.model.lesson.Lesson
import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.lesson.LessonDateTimes
import com.mospolytech.domain.schedule.model.lesson.LessonTime
import com.mospolytech.domain.schedule.model.lesson_subject.LessonSubject
import com.mospolytech.domain.schedule.model.lesson_type.LessonType
import com.mospolytech.domain.schedule.model.place.Place
import com.mospolytech.domain.schedule.model.schedule.LessonsByTime
import com.mospolytech.domain.schedule.model.schedule.ScheduleDay
import com.mospolytech.domain.schedule.model.teacher.Teacher
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.collections.ArrayList

class ApiScheduleConverter {
    companion object {
        private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }

    fun convertToLessons(scheduleResponse: ScheduleResponse): List<LessonDateTimes> {
        val lessons = scheduleResponse.contents.values.flatMap {
            convertLessons(
                it.grid.toList(),
                listOf(it.group),
                it.isSession
            )
        }
        return lessons
    }

    fun convertToLessons(scheduleResponse: ScheduleSessionResponse): List<LessonDateTimes> {
        val lessons = scheduleResponse.contents.flatMap {
            convertLessons(
                it.grid.toList(),
                listOf(it.group),
                it.isSession
            )
        }
        return lessons
    }

    private fun convertLessons(
        days: List<Pair<String, Map<String, List<ApiLesson>>>>,
        groups: List<ApiGroup>,
        isByDate: Boolean
    ): List<LessonDateTimes> {
        val groupIsEvening = (groups.firstOrNull()?.evening ?: 0) != 0

        val convertedDays = days.flatMap { (day, dailyLessons) ->
            dailyLessons.toList().flatMap {
                val (order, lessons) = it

                lessons.map { apiLesson ->
                    val orderInt = order.toIntOrNull() ?: 0
                    val (timeStart, timeEnd) = LessonTimeConverter.getLocalTime(orderInt - 1, groupIsEvening)

                    val dateFrom = parseDate(apiLesson.df, LocalDate.MIN)
                    val dateTo = parseDate(apiLesson.dt, LocalDate.MAX)
                    val dates = getDates(day, isByDate, dateFrom, dateTo)


                    convertLessonDateTimes(
                        apiLesson,
                        groups,
                        dates,
                        timeStart,
                        timeEnd
                    )
                }
            }

        }
        return convertedDays
    }

    private fun getDates(day: String, isByDate: Boolean, dateFrom: LocalDate, dateTo: LocalDate): Pair<LocalDate, LocalDate?> {
        return if (isByDate) {
            val date = LocalDate.parse(day, dateFormatter)
            date to null
        } else {
            val dayOfWeek = DayOfWeek.of(day.toIntOrNull() ?: 1)
            val firstDayOfWeek = getClosestDayNotEarly(dateFrom, dayOfWeek)
            val lastDayOfWeek = getClosestDayNotLater(dateTo, dayOfWeek)
            firstDayOfWeek to lastDayOfWeek
        }
    }

    private fun getClosestDayNotEarly(day: LocalDate, dayOfWeek: DayOfWeek): LocalDate {
        val fromDayOfWeek = day.dayOfWeek.value
        val daysToAdd = (dayOfWeek.value - fromDayOfWeek).toLong()
        return if (daysToAdd >= 0)
            day.plusDays(daysToAdd)
        else
            day.plusDays(daysToAdd + 7L)
    }

    private fun getClosestDayNotLater(day: LocalDate, dayOfWeek: DayOfWeek): LocalDate {
        val toDayOfWeek = day.dayOfWeek.value
        val daysToAdd = (dayOfWeek.value - toDayOfWeek).toLong()
        return if (daysToAdd <= 0)
            day.plusDays(daysToAdd)
        else
            day.plusDays(daysToAdd - 7L)
    }

    private fun convertLessonDateTimes(
        apiLesson: ApiLesson,
        groups: List<ApiGroup>,
        dates: Pair<LocalDate, LocalDate?>,
        timeStart: LocalTime,
        timeEnd: LocalTime
    ): LessonDateTimes {
        val lesson = convertLesson(apiLesson, groups)
        val dateTimes = convertLessonDateTime(dates, timeStart, timeEnd)

        return LessonDateTimes(
            lesson = lesson,
            time = listOf(dateTimes)
        )
    }

    private fun convertLessonDateTime(
        dates: Pair<LocalDate, LocalDate?>,
        timeStart: LocalTime,
        timeEnd: LocalTime
    ): LessonDateTime {

        return LessonDateTime(
            startDate = dates.first,
            endDate = dates.second,
            time = LessonTime(
                start = timeStart,
                end = timeEnd
            )
        )
    }

    private fun convertLesson(apiLesson: ApiLesson, apiGroups: List<ApiGroup>): Lesson {
        val subject = LessonSubjectConverter.convertTitle(apiLesson.sbj)
        val type = LessonTypeConverter.convertType(apiLesson.type, apiLesson.sbj)
        val teachers = LessonTeachersConverter.convertTeachers(apiLesson.teacher)
        val groups = LessonGroupsConverter.convertGroups(apiGroups)
        val places = LessonPlacesConverter.convertPlaces(apiLesson.auditories)

        return Lesson(
            type = LessonType.from(type),
            subject = LessonSubject.from(subject),
            teachers = teachers.map { Teacher.from(it) },
            groups = groups.map { Group.from(it) },
            places = places.map { Place.from(it) },
        )
    }

    private fun parseDate(date: String?, default: LocalDate): LocalDate {
        return if (date == null) {
            default
        } else try {
            LocalDate.parse(date, dateFormatter)
        } catch (e: DateTimeParseException) {
            default
        }
    }
}

fun mergeLessons(vararg lessonsList: List<LessonDateTimes>): List<LessonDateTimes> {
    val countTotal = lessonsList.sumOf { it.size }
    val suggestedNewCount = (countTotal * 0.85).toInt()
    val resList: MutableList<LessonDateTimes> = ArrayList(suggestedNewCount)

    for (lessons in lessonsList) {
        for (lessonDateTimes in lessons) {
            val indexToMerge = resList.indexOfFirst { lessonDateTimes.canMergeByGroup(it) }
            if (indexToMerge != -1) {
                resList[indexToMerge] = resList[indexToMerge].mergeByGroup(lessonDateTimes)
            } else {
                resList.add(lessonDateTimes)
            }
        }
    }
    resList.sort()
    return resList
}

fun LessonDateTimes.mergeByGroup(other: LessonDateTimes): LessonDateTimes {
    return this.copy(lesson = lesson.copy(groups = (lesson.groups + other.lesson.groups).sorted()))
}

fun LessonDateTimes.canMergeByGroup(other: LessonDateTimes): Boolean {
    return lesson.canMergeByGroup(other.lesson) &&
            time == other.time
}

fun Lesson.canMergeByGroup(other: Lesson): Boolean {
    return subject == other.subject &&
            places == other.places &&
            teachers == other.teachers
}

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

fun getLessonDateRange(lessons: List<LessonDateTimes>): Pair<LocalDate, LocalDate> {
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