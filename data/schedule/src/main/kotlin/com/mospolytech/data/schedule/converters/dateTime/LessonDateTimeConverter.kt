package com.mospolytech.data.schedule.converters.dateTime

import com.mospolytech.domain.base.utils.MAX
import com.mospolytech.domain.base.utils.MIN
import com.mospolytech.domain.base.utils.Moscow
import com.mospolytech.domain.schedule.model.pack.LessonDateTime
import com.mospolytech.domain.schedule.model.rrule.Frequency
import com.mospolytech.domain.schedule.model.rrule.RRule
import kotlinx.datetime.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.time.Duration
import java.time.LocalDate as JavaLocalDate

class LessonDateTimeConverter {
    fun convertDateTime(rawDateTime: ApiDateTimeData): LessonParserDateTime {
        val orderInt = rawDateTime.order.toIntOrNull() ?: 0
        val (timeStart, timeEnd) = LessonTimeConverter.getLocalTime(orderInt - 1, rawDateTime.groupIsEvening)

        val dateFrom = parseDate(rawDateTime.apiLesson.df, LocalDate.MIN)
        val dateTo = parseDate(rawDateTime.apiLesson.dt, LocalDate.MAX)
        val dates = getDates(rawDateTime.day, rawDateTime.isByDate, dateFrom, dateTo)
        val (realDateFrom, realDateTo) = dates

        val start =
            LessonDateTime(
                dateTime = realDateFrom.atTime(timeStart),
                timeZone = TimeZone.Moscow,
            )

        val end =
            LessonDateTime(
                dateTime = realDateFrom.atTime(timeEnd),
                timeZone = TimeZone.Moscow,
            )

        val rrule =
            if (realDateTo == null) {
                null
            } else {
                RRule().apply {
                    freq = Frequency.Weekly
                    until = realDateTo.atTime(LocalTime.MAX).toInstant(TimeZone.UTC)
                }.toRFC5545String()
            }

        return LessonParserDateTime(
            start = start,
            end = end,
            recurrence = rrule,
        )
    }

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    private fun getDates(
        day: String,
        isByDate: Boolean,
        dateFrom: LocalDate,
        dateTo: LocalDate,
    ): Pair<LocalDate, LocalDate?> {
        return if (isByDate) {
            val date = JavaLocalDate.parse(day, dateFormatter).toKotlinLocalDate()
            date to null
        } else {
            val dayOfWeek = DayOfWeek.of(day.toIntOrNull() ?: 1)
            val firstDayOfWeek = getClosestDayNotEarly(dateFrom, dayOfWeek)
            val lastDayOfWeek = getClosestDayNotLater(dateTo, dayOfWeek)
            firstDayOfWeek to lastDayOfWeek
        }
    }

    private fun getClosestDayNotEarly(
        day: LocalDate,
        dayOfWeek: DayOfWeek,
    ): LocalDate {
        val fromDayOfWeek = day.dayOfWeek.value
        val daysToAdd = (dayOfWeek.value - fromDayOfWeek).toLong()
        return if (daysToAdd >= 0) {
            day + daysToAdd.toDayPeriod()
        } else {
            day + (daysToAdd + 7L).toDayPeriod()
        }
    }

    private fun getClosestDayNotLater(
        day: LocalDate,
        dayOfWeek: DayOfWeek,
    ): LocalDate {
        val toDayOfWeek = day.dayOfWeek.value
        val daysToAdd = (dayOfWeek.value - toDayOfWeek).toLong()
        return if (daysToAdd <= 0) {
            day + daysToAdd.toDayPeriod()
        } else {
            day + (daysToAdd - 7L).toDayPeriod()
        }
    }

    private fun Long.toDayPeriod(): DatePeriod {
        return DatePeriod(days = toInt())
    }

    private fun Duration.toDatePeriod(): DatePeriod {
        return DatePeriod(
            days = this.inWholeDays.toInt(),
        )
    }

    private fun parseDate(
        date: String?,
        default: LocalDate,
    ): LocalDate {
        return if (date == null) {
            default
        } else {
            try {
                JavaLocalDate.parse(date, dateFormatter).toKotlinLocalDate()
            } catch (e: DateTimeParseException) {
                default
            }
        }
    }

    class LessonParserDateTime(
        val start: LessonDateTime,
        val end: LessonDateTime,
        val recurrence: String?,
    )
}
