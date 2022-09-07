package com.mospolytech.data.schedule.converters

import java.time.LocalTime

object LessonTimeConverter {
    enum class LessonTimes(
        val start: LocalTime,
        val end: LocalTime
    ) {
        Pair1(
            LocalTime.of(9, 0),
            LocalTime.of(10, 30)
        ),
        Pair2(
            LocalTime.of(10, 40),
            LocalTime.of(12, 10)
        ),
        Pair3(
            LocalTime.of(12, 20),
            LocalTime.of(13, 50)
        ),
        Pair4(
            LocalTime.of(14, 30),
            LocalTime.of(16, 0)
        ),
        Pair5(
            LocalTime.of(16, 10),
            LocalTime.of(17, 40)
        ),
        Pair6(
            LocalTime.of(17, 50),
            LocalTime.of(19, 20)
        ),
        Pair6Evening(
            LocalTime.of(18, 20),
            LocalTime.of(19, 40)
        ),
        Pair7(
            LocalTime.of(19, 30),
            LocalTime.of(21, 0)
        ),
        Pair7Evening(
            LocalTime.of(19, 50),
            LocalTime.of(21, 10)
        ),
        Undefined(
            LocalTime.MIN,
            LocalTime.MAX
        );

        operator fun component1(): LocalTime {
            return start
        }
        operator fun component2(): LocalTime {
            return end
        }
    }

    fun getLocalTime(order: Int, groupIsEvening: Boolean) = when (order) {
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
