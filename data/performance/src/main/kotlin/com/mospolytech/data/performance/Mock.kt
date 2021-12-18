package com.mospolytech.data.performance

import com.mospolytech.domain.perfomance.model.ExamType.*
import com.mospolytech.domain.perfomance.model.Mark
import com.mospolytech.domain.perfomance.model.Marks
import java.time.LocalDateTime

fun getMarksList(): List<Marks> =
    listOf(Marks(1, 1, getMark()),
    Marks(1, 2, getMark()),
    Marks(2, 3, getMark()),
    Marks(2, 4, getMark()))

fun getMark(): List<Mark> =
    listOf(Mark("1", "ИСИТ", Exam, "5", LocalDateTime.now()),
        Mark("2", "ИПИТ", MarkPass, "4", LocalDateTime.now()),
        Mark("3", "ТИПИС", CourseWork, "3", LocalDateTime.now()),
        Mark("4", "МИСИПИТ", Pass, "Зачет", LocalDateTime.now()))