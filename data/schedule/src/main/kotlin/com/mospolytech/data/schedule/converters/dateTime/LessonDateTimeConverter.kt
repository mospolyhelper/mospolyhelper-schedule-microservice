package com.mospolytech.data.schedule.converters.dateTime

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.LessonDateTimesDb
import com.mospolytech.data.schedule.model.entity.LessonDateTimeEntity
import com.mospolytech.domain.base.utils.MAX
import com.mospolytech.domain.base.utils.MIN
import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import com.mospolytech.domain.schedule.model.lesson.LessonTime
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.batchInsert
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.time.Duration
import java.time.LocalDate as JavaLocalDate

class LessonDateTimeConverter {
    private val converterCache = HashMap<ApiDateTimeData, LessonDateTime>()
    private val dbCache = HashMap<LessonDateTime, String>()

    private fun convertDateTime(rawDateTime: ApiDateTimeData): LessonDateTime {
        return converterCache.getOrPut(rawDateTime) {
            val orderInt = rawDateTime.order.toIntOrNull() ?: 0
            val (timeStart, timeEnd) = LessonTimeConverter.getLocalTime(orderInt - 1, rawDateTime.groupIsEvening)

            val dateFrom = parseDate(rawDateTime.apiLesson.df, LocalDate.MIN)
            val dateTo = parseDate(rawDateTime.apiLesson.dt, LocalDate.MAX)
            val dates = getDates(rawDateTime.day, rawDateTime.isByDate, dateFrom, dateTo)

            convertLessonDateTime(
                dates = dates,
                timeStart = timeStart,
                timeEnd = timeEnd,
            )
        }
    }

    fun getCachedId(rawDateTime: ApiDateTimeData): String {
        val dtoCache = checkNotNull(converterCache[rawDateTime])
        return checkNotNull(dbCache[dtoCache])
    }

    suspend fun cacheAll(rawDateTimes: Set<ApiDateTimeData>) {
        MosPolyDb.transaction {
            val allDbItems = LessonDateTimeEntity.all().map { cacheDb(it) }.toSet()

            val dtoList = rawDateTimes.map { convertDateTime(it) }

            val notInDb = dtoList subtract allDbItems

            val rows =
                LessonDateTimesDb.batchInsert(notInDb) { dto ->
                    this[LessonDateTimesDb.startDate] = dto.startDate
                    this[LessonDateTimesDb.endDate] = dto.endDate
                    this[LessonDateTimesDb.startTime] = dto.time.start
                    this[LessonDateTimesDb.endTime] = dto.time.end
                }

            LessonDateTimeEntity.wrapRows(SizedCollection(rows)).forEach { cacheDb(it) }
        }
    }

    @Suppress("UnnecessaryVariable")
    private fun cacheDb(entity: LessonDateTimeEntity): LessonDateTime {
        val model = entity.toModel()
        val modelWithoutId = model
        dbCache[modelWithoutId] = entity.id.toString()
        return modelWithoutId
    }

    fun clearCache() {
        converterCache.clear()
        dbCache.clear()
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

    private fun convertLessonDateTime(
        dates: Pair<LocalDate, LocalDate?>,
        timeStart: LocalTime,
        timeEnd: LocalTime,
    ): LessonDateTime {

        return LessonDateTime(
            startDate = dates.first,
            endDate = dates.second,
            time =
                LessonTime(
                    start = timeStart,
                    end = timeEnd,
                ),
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
}
