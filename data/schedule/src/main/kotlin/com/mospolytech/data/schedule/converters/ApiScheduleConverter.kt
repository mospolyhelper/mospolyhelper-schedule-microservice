package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.ApiGroup
import com.mospolytech.data.schedule.model.ApiLesson
import com.mospolytech.data.schedule.model.ScheduleResponse
import com.mospolytech.data.schedule.model.ScheduleSessionResponse
import com.mospolytech.domain.schedule.model.*
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

    val globalLocations = mutableSetOf<String>()

    fun printGlobalData() {
        println(globalLocations)
    }


    fun convertToLessons(scheduleResponse: ScheduleResponse): List<LessonDateTimes> {
        val lessons = scheduleResponse.contents.values.flatMap {
            convertLessons(
                it.grid.toList(),
                listOf(it.group),
                it.isSession
            )
        }
        printGlobalData()
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
        printGlobalData()
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

    private fun getDates(day: String, isByDate: Boolean, dateFrom: LocalDate, dateTo: LocalDate): List<LocalDate> {
        if (isByDate) {
            val date = LocalDate.parse(day, dateFormatter)
            return listOf(date)
        } else {
            val dayOfWeek = DayOfWeek.of(day.toIntOrNull() ?: 1)
            val dateFromDayOfWeek = dateFrom.dayOfWeek.value
            var daysToAdd = (dayOfWeek.value - dateFromDayOfWeek)
            if (daysToAdd < 0) daysToAdd += 7
            val firstDayOfWeek = dateFrom.plusDays(daysToAdd.toLong())
            val dates = mutableListOf<LocalDate>()
            var currentDay = firstDayOfWeek
            do {
                dates += currentDay
                currentDay = currentDay.plusDays(7)
            } while (currentDay <= dateTo)
            return dates
        }
    }

    private fun convertLessonDateTimes(
        apiLesson: ApiLesson,
        groups: List<ApiGroup>,
        dates: List<LocalDate>,
        timeStart: LocalTime,
        timeEnd: LocalTime
    ): LessonDateTimes {
        val lesson = convertLesson(apiLesson, groups)
        val dateTimes = convertLessonDateTime(dates, timeStart, timeEnd)

        return LessonDateTimes(
            lesson = lesson,
            time = dateTimes
        )
    }

    private fun convertLessonDateTime(
        dates: List<LocalDate>,
        timeStart: LocalTime,
        timeEnd: LocalTime
    ): List<LessonDateTime> {
        val dateTimes = dates.map { date ->
            LessonDateTime(
                date = date,
                time = LessonTime(
                    startTime = timeStart,
                    endTime = timeEnd
                )
            )
        }

        return dateTimes
    }

    private fun convertLesson(apiLesson: ApiLesson, apiGroups: List<ApiGroup>): Lesson {
        val title = LessonTitleConverter.convertTitle(apiLesson.sbj)
        val type = LessonTypeConverter.convertType(apiLesson.type, apiLesson.sbj)
        val teachers = LessonTeachersConverter.convertTeachers(apiLesson.teacher)
        val groups = LessonGroupsConverter.convertGroups(apiGroups)
        val places = LessonPlacesConverter.convertPlaces(apiLesson.auditories)

        globalLocations.add(apiLesson.location)

        return Lesson(
            title = title,
            type = type,
            teachers = teachers,
            groups = groups,
            places = places
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
    return this.copy(lesson = lesson.copy(groups = lesson.groups + other.lesson.groups))
}

fun LessonDateTimes.canMergeByGroup(other: LessonDateTimes): Boolean {
    return lesson.canMergeByGroup(other.lesson) &&
            time == other.time
}

fun Lesson.canMergeByGroup(other: Lesson): Boolean {
    return title == other.title &&
            places == other.places &&
            teachers == other.teachers
}

fun buildSchedule(
    lessons: List<LessonDateTimes>,
    dateFrom: LocalDate,
    dateTo: LocalDate
): List<ScheduleDay> {
    val resMap: MutableMap<LocalDate, MutableMap<LessonTime, MutableList<Lesson>>> = TreeMap()

    for (lessonDateTimes in lessons) {
        for (dateTime in lessonDateTimes.time) {
            val timeToLessonsMap = resMap[dateTime.date]
            if (timeToLessonsMap != null) {
                val lessonList = timeToLessonsMap[dateTime.time]
                if (lessonList != null) {
                    lessonList.add(lessonDateTimes.lesson)
                } else {
                    timeToLessonsMap[dateTime.time] = mutableListOf(lessonDateTimes.lesson)
                }
            } else {
                resMap[dateTime.date] = TreeMap<LessonTime, MutableList<Lesson>>().apply {
                    set(dateTime.time, mutableListOf(lessonDateTimes.lesson))
                }
            }
        }
    }

    var currentDay = dateFrom
    do {
        if (!resMap.containsKey(currentDay)) {
            resMap[currentDay]
        }
        currentDay = currentDay.plusDays(7)
    } while (currentDay <= dateTo)


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
            if (dateTime.date < minDate) {
                minDate = dateTime.date
            }

            if (dateTime.date > maxDate) {
                maxDate = dateTime.date
            }
        }
    }

    return minDate to maxDate
}