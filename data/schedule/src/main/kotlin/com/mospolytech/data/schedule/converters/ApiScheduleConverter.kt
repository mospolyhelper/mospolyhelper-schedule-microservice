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
    private val lessonDateTimeConverter: LessonDateTimeConverter,
    private val lessonConverter: LessonConverter
) {
    suspend fun convertToLessons(scheduleResponse: ScheduleResponse) {

        // TODO PROGRESS
        scheduleResponse.contents.values.forEach {
            convertLessons(
                it.grid.toList(),
                listOf(it.group),
                it.isSession
            )
        }
    }

    suspend fun convertToLessons(scheduleResponse: ScheduleSessionResponse) {
        scheduleResponse.contents.forEach {
            convertLessons(
                it.grid.toList(),
                listOf(it.group),
                it.isSession
            )
        }
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
                    val timesId =
                        lessonDateTimeConverter.convertDateTime(order, groupIsEvening, apiLesson, day, isByDate)

                    lessonConverter.convertLesson(apiLesson, groups, listOf(timesId))
                }
            }
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