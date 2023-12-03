package com.mospolytech.data.personal

import com.mospolytech.domain.personal.Order
import com.mospolytech.domain.personal.Personal
import com.mospolytech.domain.personal.Subdivision
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter
import java.time.LocalDate as JavaLocalDate

@Serializable
data class PersonalResponse(
    val user: User,
) {
    @Serializable
    data class User(
        val id: Int,
        @SerialName("user_status")
        val userStatus: String,
        val status: String,
        val course: String,
        val name: String,
        val surname: String,
        val patronymic: String,
        val avatar: String,
        val birthday: String,
        val sex: String,
        val code: String,
        val faculty: String,
        val group: String,
        val specialty: String,
        val specialization: String,
        val degreeLength: String,
        val educationForm: String,
        val finance: String,
        val degreeLevel: String,
        val enterYear: String,
        val orders: List<String>,
        val subdivisions: List<Subdivision>? = null,
    )

    @Serializable
    data class Subdivision(
        val categoty: String,
        val jobType: String? = null,
        val status: String? = null,
        val subdivision: String? = null,
        val wage: String? = null,
    )
}

fun PersonalResponse.toModel(): Personal {
    val fullName = this.user.surname + " " + this.user.name + " " + this.user.patronymic
    val isStudent = user.userStatus != "staff"
    val description =
        if (isStudent) {
            "Студент • ${user.group} • ${user.degreeLevel.lowercase()} "
        } else {
            "Преподаватель"
        }
    return Personal(
        id = this.user.id.toString(),
        name = fullName,
        description = description,
        avatar = this.user.avatar,
        data =
            buildMap {
                put("Статус", user.status)
                put("Курс", user.group)
                put("Дата рождения", user.birthday)
                put("Пол", user.sex.getSex())
                put("Код студента", user.code)
                put("Факультет", user.faculty)
                put("Группа", user.group)
                put("Направление", user.specialty)
                user.specialization.ifEmpty { null }?.let { specialization ->
                    put("Специализация", specialization)
                }
                put("Срок обучения", user.degreeLength.filter { it.isDigit() })
                put("Форма обучения", user.educationForm)
                put("Вид финансирования", user.finance)
                put("Год набора", user.enterYear)
            },
    )
}

private fun String.getSex() =
    when {
        contains("female", ignoreCase = true) -> "Женский"
        contains("male", ignoreCase = true) -> "Мужской"
        else -> this
    }

private fun String.toModel(): Order {
    val dateRegex = "от.*г\\.".toRegex()
    val nameRegex = "^.* от".toRegex()
    val descriptionRegex = "«.*»".toRegex()

    val description =
        descriptionRegex
            .find(this)
            ?.value
            ?.replace("«", "")
            ?.replace("»", "")
            ?.trim()
            .orEmpty()
    val name =
        nameRegex.find(this)
            ?.value
            ?.replace("от", "")
            ?.trim()
            .orEmpty()
    val date =
        dateRegex.find(this)
            ?.value
            ?.toDate()

    return Order(date, name, description)
}

fun String.toDate(): LocalDate? {
    return try {
        JavaLocalDate.parse(this, DateTimeFormatter.ofPattern("'от' d MMMM yyyy 'г.'"))
            .toKotlinLocalDate()
    } catch (e: Throwable) {
        null
    }
}

private fun PersonalResponse.Subdivision.toModel(): Subdivision {
    return Subdivision(
        category = this.categoty,
        jobType = this.jobType,
        status = this.status,
        subdivision = this.subdivision,
        wage = this.wage,
    )
}
