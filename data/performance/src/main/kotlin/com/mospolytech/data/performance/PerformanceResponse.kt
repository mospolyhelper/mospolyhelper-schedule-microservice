package com.mospolytech.data.performance

import com.mospolytech.domain.perfomance.model.Grade
import com.mospolytech.domain.perfomance.model.GradeValue
import com.mospolytech.domain.perfomance.model.Performance
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Serializable
data class PerformanceResponseDto(
    val academicPerformance: List<PerformanceResponse>,
)

@Serializable
data class PerformanceResponse(
    val id: Int,
    @SerialName("bill_num")
    val billNum: String,
    @SerialName("bill_type")
    val billType: String,
    @SerialName("doc_type")
    val docType: String,
    val name: String,
    @SerialName("exam_date")
    val examDate: String,
    @SerialName("exam_time")
    val examTime: String,
    val grade: String,
    @SerialName("ticket_num")
    val ticketNum: String,
    val teacher: String,
    val course: Int,
    @SerialName("exam_type")
    val examType: String,
    val chair: String,
)

fun PerformanceResponse.toModel(): Performance {
    val grade = convertGrade(grade)
    val teacherName = getShortName(teacher)
    val date = examDate.toDate()?.toRussianText()

    val description =
        if (date == null) {
            teacherName
        } else {
            "$teacherName • $date"
        }

    return Performance(
        id = id.toString(),
        title = name,
        type = examType,
        description = description,
        grade = grade,
    )
}

private fun getShortName(fullName: String): String {
    val names = fullName.split(" ").filter { it.isNotEmpty() }
    return if (names.isEmpty()) {
        return fullName
    } else {
        buildString {
            append(names.first())
            for (index in 1..names.lastIndex) {
                append(" ")
                append(names[index].first())
                append('.')
            }
        }
    }
}

private fun convertGrade(grade: String): Grade {
    return gradePool.getOrPut(grade) { parseGrade(grade) }
}

private val gradePool = hashMapOf<String, Grade>()

private fun parseGrade(grade: String): Grade {
    val value: GradeValue?
    val title: String
    when (grade) {
        "Зачтено" -> {
            title = "✔"
            value = GradeValue.VERY_GOOD
        }
        "Не зачтено" -> {
            title = "×"
            value = GradeValue.VERY_BAD
        }
        "Отлично" -> {
            title = "5"
            value = GradeValue.VERY_GOOD
        }
        "Хорошо" -> {
            title = "4"
            value = GradeValue.GOOD
        }
        "Удовлетворительно" -> {
            title = "3"
            value = GradeValue.BAD
        }
        "Не явился" -> {
            title = "Неявка"
            value = GradeValue.VERY_BAD
        }
        "Неудовлетворительно" -> {
            title = "2"
            value = GradeValue.VERY_BAD
        }
        else -> {
            title = grade
            value = null
        }
    }
    return Grade(
        title = title,
        value = value,
    )
}

private val dateFormatter = DateTimeFormatter.ofPattern("MMMM d',' yyyy", Locale.US)
private val dateFormatterRu =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
        .withLocale(Locale("ru", "RU"))

fun String.toDate(): LocalDate? {
    return try {
        LocalDate.parse(this, dateFormatter)
    } catch (e: Throwable) {
        null
    }
}

fun LocalDate.toRussianText(): String {
    return dateFormatterRu.format(this)
}

fun String.toTime(): LocalTime? {
    return try {
        LocalTime.parse(this)
    } catch (e: Throwable) {
        null
    }
}
