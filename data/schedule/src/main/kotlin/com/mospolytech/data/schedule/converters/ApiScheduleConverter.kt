package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.response.ApiGroup
import com.mospolytech.data.schedule.model.response.ApiLesson
import com.mospolytech.data.schedule.model.response.ScheduleResponse
import com.mospolytech.data.schedule.model.response.ScheduleSessionResponse
import com.mospolytech.data.schedule.remote.LessonDateTimesRemoteDS
import com.mospolytech.data.schedule.remote.LessonsRemoteDS
import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.lesson.LessonTime
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.pack.CompactLessonFeatures
import kotlinx.datetime.toKotlinLocalDate
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ApiScheduleConverter(
    private val lessonSubjectsConverter: LessonSubjectConverter,
    private val lessonTypeConverter: LessonTypeConverter,
    private val teachersConverter: LessonTeachersConverter,
    private val groupsConverter: LessonGroupsConverter,
    private val placesConverter: LessonPlacesConverter,
    private val lessonsRemoteDS: LessonsRemoteDS,
    private val lessonDateTimesRemoteDS: LessonDateTimesRemoteDS
) {
    companion object {
        private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }

    suspend fun convertToLessons(scheduleResponse: ScheduleResponse) {
        val lessons = scheduleResponse.contents.values.forEach {
            convertLessons(
                it.grid.toList(),
                listOf(it.group),
                it.isSession
            )
        }
        return lessons
    }

    suspend fun convertToLessons(scheduleResponse: ScheduleSessionResponse) {
        val lessons = scheduleResponse.contents.forEach {
            convertLessons(
                it.grid.toList(),
                listOf(it.group),
                it.isSession
            )
        }
        return lessons
    }

    private suspend fun convertLessons(
        days: List<Pair<String, Map<String, List<ApiLesson>>>>,
        groups: List<ApiGroup>,
        isByDate: Boolean
    ) {
        val groupIsEvening = (groups.firstOrNull()?.evening ?: 0) != 0

        days.forEach { (day, dailyLessons) ->
            dailyLessons.forEach {
                val (order, lessons) = it
                lessons.forEach { apiLesson ->
                    val orderInt = order.toIntOrNull() ?: 0
                    val (timeStart, timeEnd) = LessonTimeConverter.getLocalTime(orderInt - 1, groupIsEvening)

                    val dateFrom = parseDate(apiLesson.df, LocalDate.MIN)
                    val dateTo = parseDate(apiLesson.dt, LocalDate.MAX)
                    val dates = getDates(day, isByDate, dateFrom, dateTo)

                    val lessonPack = convertLessonDateTimes(
                        apiLesson,
                        groups,
                        dates,
                        timeStart,
                        timeEnd
                    )

                    val timesId = lessonDateTimesRemoteDS.add(lessonPack.times)

                    lessonsRemoteDS.add(
                        typeId = lessonPack.lesson.typeId,
                        subjectId = lessonPack.lesson.subjectId,
                        teachersId = lessonPack.lesson.teachersId,
                        groupsId = lessonPack.lesson.groupsId,
                        placesId = lessonPack.lesson.placesId,
                        lessonDateTimesId = timesId
                    )
                }
            }
        }
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

    private suspend fun convertLessonDateTimes(
        apiLesson: ApiLesson,
        groups: List<ApiGroup>,
        dates: Pair<LocalDate, LocalDate?>,
        timeStart: LocalTime,
        timeEnd: LocalTime
    ): CompactLessonAndTimes {
        val lesson = convertLesson(apiLesson, groups)
        val dateTimes = convertLessonDateTime(dates, timeStart, timeEnd)

        return CompactLessonAndTimes(
            lesson = lesson,
            times = listOf(dateTimes)
        )
    }

    private fun convertLessonDateTime(
        dates: Pair<LocalDate, LocalDate?>,
        timeStart: LocalTime,
        timeEnd: LocalTime
    ): LessonDateTime {

        return LessonDateTime(
            startDate = dates.first.toKotlinLocalDate(),
            endDate = dates.second?.toKotlinLocalDate(),
            time = LessonTime(
                start = timeStart,
                end = timeEnd
            )
        )
    }

    private suspend fun convertLesson(apiLesson: ApiLesson, apiGroups: List<ApiGroup>): CompactLessonFeatures {
        val subject = lessonSubjectsConverter.convertTitle(apiLesson.sbj)
        val type = lessonTypeConverter.convertType(apiLesson.type, apiLesson.sbj)
        val teacherIds = teachersConverter.convertTeachers(apiLesson.teacher)
        val groups = groupsConverter.convertGroups(apiGroups)
        val places = placesConverter.convertPlaces(apiLesson.auditories)

        return CompactLessonFeatures(
            id = "",
            typeId = type,
            subjectId = subject,
            teachersId = teacherIds,
            groupsId = groups,
            placesId = places,
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

fun mergeLessons(vararg lessonsList: List<CompactLessonAndTimes>): List<CompactLessonAndTimes> {
    val countTotal = lessonsList.sumOf { it.size }
    val suggestedNewCount = (countTotal * 0.85).toInt()
    val resList: MutableList<CompactLessonAndTimes> = ArrayList(suggestedNewCount)

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
    //resList.sort()
    return resList
}

fun CompactLessonAndTimes.mergeByGroup(other: CompactLessonAndTimes): CompactLessonAndTimes {
    return this.copy(
        lesson = lesson.copy(
            groupsId = (lesson.groupsId + other.lesson.groupsId).sorted()
        )
    )
}

fun CompactLessonAndTimes.canMergeByGroup(other: CompactLessonAndTimes): Boolean {
    return lesson.canMergeByGroup(other.lesson) &&
            times == other.times
}

fun CompactLessonFeatures.canMergeByGroup(other: CompactLessonFeatures): Boolean {
    return subjectId == other.subjectId &&
            placesId == other.placesId &&
            teachersId == other.teachersId
}