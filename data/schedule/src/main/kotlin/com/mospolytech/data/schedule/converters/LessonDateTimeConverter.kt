package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.response.ApiLesson
import com.mospolytech.data.schedule.remote.LessonDateTimesRemoteDS
import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.lesson.LessonTime
import kotlinx.datetime.toKotlinLocalDate
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class LessonDateTimeConverter(
    private val lessonDateTimesRemoteDS: LessonDateTimesRemoteDS
) {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    suspend fun convertDateTime(
        order: String,
        groupIsEvening: Boolean,
        apiLesson: ApiLesson,
        day: String,
        isByDate: Boolean
    ): String {
        val orderInt = order.toIntOrNull() ?: 0
        val (timeStart, timeEnd) = LessonTimeConverter.getLocalTime(orderInt - 1, groupIsEvening)

        val dateFrom = parseDate(apiLesson.df, LocalDate.MIN)
        val dateTo = parseDate(apiLesson.dt, LocalDate.MAX)
        val dates = getDates(day, isByDate, dateFrom, dateTo)


        val dateTimes = convertLessonDateTime(
            dates,
            timeStart,
            timeEnd
        )
        return lessonDateTimesRemoteDS.add(dateTimes)
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