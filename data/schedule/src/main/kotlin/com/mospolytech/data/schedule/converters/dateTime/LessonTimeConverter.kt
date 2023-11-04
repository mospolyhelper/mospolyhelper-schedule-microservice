package com.mospolytech.data.schedule.converters.dateTime

import com.mospolytech.domain.base.utils.MAX
import com.mospolytech.domain.base.utils.MIN
import kotlinx.datetime.LocalTime

object LessonTimeConverter {
    enum class LessonTimes(
        val start: LocalTime,
        val end: LocalTime,
    ) {
        Pair1(
            LocalTime(9, 0),
            LocalTime(10, 30),
        ),
        Pair2(
            LocalTime(10, 40),
            LocalTime(12, 10),
        ),
        Pair3(
            LocalTime(12, 20),
            LocalTime(13, 50),
        ),
        Pair4(
            LocalTime(14, 30),
            LocalTime(16, 0),
        ),
        Pair5(
            LocalTime(16, 10),
            LocalTime(17, 40),
        ),
        Pair6(
            LocalTime(17, 50),
            LocalTime(19, 20),
        ),
        Pair6Evening(
            LocalTime(18, 20),
            LocalTime(19, 40),
        ),
        Pair7(
            LocalTime(19, 30),
            LocalTime(21, 0),
        ),
        Pair7Evening(
            LocalTime(19, 50),
            LocalTime(21, 10),
        ),
        Undefined(
            LocalTime.MIN,
            LocalTime.MAX,
        ),
        ;

        operator fun component1(): LocalTime {
            return start
        }

        operator fun component2(): LocalTime {
            return end
        }
    }

    fun getLocalTime(
        order: Int,
        groupIsEvening: Boolean,
    ) = when (order) {
        0 -> LessonTimes.Pair1
        1 -> LessonTimes.Pair2
        2 -> LessonTimes.Pair3
        3 -> LessonTimes.Pair4
        4 -> LessonTimes.Pair5
        5 -> if (groupIsEvening) LessonTimes.Pair6Evening else LessonTimes.Pair6
        6 -> if (groupIsEvening) LessonTimes.Pair7Evening else LessonTimes.Pair7
        else -> {
            LessonTimes.Undefined
        }
    }
}
