package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.converters.dateTime.ApiDateTimeData
import com.mospolytech.data.schedule.converters.dateTime.LessonDateTimeConverter
import com.mospolytech.data.schedule.converters.lessons.ApiLessonData
import com.mospolytech.data.schedule.converters.lessons.LessonConverter
import com.mospolytech.data.schedule.model.response.ApiGroup
import com.mospolytech.data.schedule.model.response.ApiLesson
import com.mospolytech.data.schedule.model.response.ScheduleResponse
import com.mospolytech.data.schedule.model.response.ScheduleSessionResponse
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes
import com.mospolytech.domain.schedule.model.pack.CompactLessonFeatures
import org.slf4j.LoggerFactory

class ApiScheduleConverter(
    private val lessonDateTimeConverter: LessonDateTimeConverter,
    private val lessonConverter: LessonConverter,
) {
    private val logger = LoggerFactory.getLogger("com.mospolytech.data.schedule.converters")

    suspend fun convertToLessons(scheduleResponse: ScheduleResponse) {
        val lessons = scheduleResponse.contents

        val lessonData = ApiLessonData()

        lessons.forEach { (_, apiSchedule) ->
            lessonData.groups += apiSchedule.group.title

            apiSchedule.grid.forEach { (day, dailyLessons) ->
                dailyLessons.forEach { (order, lessonsPerOrder) ->
                    lessonsPerOrder.forEach { lesson ->
                        lessonData.titles += lesson.sbj
                        lessonData.types += lesson.type to lesson.sbj

                        lessonData.teachers += lesson.teacher

                        lesson.auditories.forEach { auditory ->
                            lessonData.places += auditory.title
                        }

                        val groupIsEvening = apiSchedule.group.evening != 0
                        val isByDate = apiSchedule.isSession

                        lessonData.dateTimes +=
                            ApiDateTimeData(
                                order = order,
                                groupIsEvening = groupIsEvening,
                                apiLesson = lesson,
                                day = day,
                                isByDate = isByDate,
                            )
                    }
                }
            }
        }
        logger.debug("Schedule: caching all lesson data")
        lessonConverter.cacheAll(lessonData)

        lessons.entries.forEachIndexed { index, (_, apiSchedule) ->
            logger.debug("Schedule: ${index + 1} / ${lessons.size}")
            convertAndCacheLessons(
                apiSchedule.grid.toList(),
                listOf(apiSchedule.group),
                apiSchedule.isSession,
            )
        }
    }

    suspend fun convertToLessons(scheduleResponse: ScheduleSessionResponse) {
        val lessons = scheduleResponse.contents

        val lessonData = ApiLessonData()

        lessons.forEach { apiSchedule ->
            lessonData.groups += apiSchedule.group.title

            apiSchedule.grid.forEach { (day, dailyLessons) ->
                dailyLessons.forEach { (order, lessonsPerOrder) ->
                    lessonsPerOrder.forEach { lesson ->
                        lessonData.titles += lesson.sbj
                        lessonData.types += lesson.type to lesson.sbj

                        lessonData.teachers += lesson.teacher

                        lesson.auditories.forEach { auditory ->
                            lessonData.places += auditory.title
                        }

                        val groupIsEvening = apiSchedule.group.evening != 0
                        val isByDate = apiSchedule.isSession

                        lessonData.dateTimes +=
                            ApiDateTimeData(
                                order = order,
                                groupIsEvening = groupIsEvening,
                                apiLesson = lesson,
                                day = day,
                                isByDate = isByDate,
                            )
                    }
                }
            }
        }
        logger.debug("Schedule Session: caching all lesson data")
        lessonConverter.cacheAll(lessonData)

        lessons.forEachIndexed { index, apiSchedule ->
            logger.debug("Schedule Session: ${index + 1} / ${lessons.size}")
            convertAndCacheLessons(
                apiSchedule.grid.toList(),
                listOf(apiSchedule.group),
                apiSchedule.isSession,
            )
        }
    }

    suspend fun pushALlLessons() {
        lessonConverter.pushAllLessons()
    }

    fun clearCache() {
        lessonConverter.clearCache()
    }

    private fun convertAndCacheLessons(
        days: List<Pair<String, Map<String, List<ApiLesson>>>>,
        groups: List<ApiGroup>,
        isByDate: Boolean,
    ) {
        val groupIsEvening = (groups.firstOrNull()?.evening ?: 0) != 0

        days.forEach { (day, dailyLessons) ->
            dailyLessons.forEach { (order, lessons) ->
                lessons.forEach { apiLesson ->
                    val timesId =
                        lessonDateTimeConverter.getCachedId(
                            ApiDateTimeData(
                                order = order,
                                groupIsEvening = groupIsEvening,
                                apiLesson = apiLesson,
                                day = day,
                                isByDate = isByDate,
                            ),
                        )

                    lessonConverter.cacheLesson(apiLesson, groups, listOf(timesId))
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
    // resList.sort()
    return resList
}

fun CompactLessonAndTimes.mergeByGroup(other: CompactLessonAndTimes): CompactLessonAndTimes {
    return this.copy(
        lesson =
            lesson.copy(
                groupsId = (lesson.groupsId + other.lesson.groupsId).sorted(),
            ),
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
