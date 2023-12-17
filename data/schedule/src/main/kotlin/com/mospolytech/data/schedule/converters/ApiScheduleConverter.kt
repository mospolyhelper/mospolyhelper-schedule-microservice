package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.converters.dateTime.ApiDateTimeData
import com.mospolytech.data.schedule.converters.dateTime.LessonDateTimeConverter
import com.mospolytech.data.schedule.converters.lessons.ApiLessonData
import com.mospolytech.data.schedule.converters.lessons.LessonConverter
import com.mospolytech.data.schedule.model.response.ApiGroup
import com.mospolytech.data.schedule.model.response.ApiLesson
import com.mospolytech.data.schedule.model.response.ScheduleResponse
import com.mospolytech.data.schedule.model.response.ScheduleSessionResponse
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
            lessonData.groups += apiSchedule.group.title to apiSchedule.group.course

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
            lessonData.groups += apiSchedule.group.title to apiSchedule.group.course

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
                    val lessonDateTime =
                        lessonDateTimeConverter.convertDateTime(
                            ApiDateTimeData(
                                order = order,
                                groupIsEvening = groupIsEvening,
                                apiLesson = apiLesson,
                                day = day,
                                isByDate = isByDate,
                            ),
                        )

                    lessonConverter.cacheLesson(
                        apiLesson = apiLesson,
                        apiGroups = groups,
                        startDateTime = lessonDateTime.start,
                        endDateTime = lessonDateTime.end,
                        recurrence = lessonDateTime.recurrence,
                    )
                }
            }
        }
    }
}
