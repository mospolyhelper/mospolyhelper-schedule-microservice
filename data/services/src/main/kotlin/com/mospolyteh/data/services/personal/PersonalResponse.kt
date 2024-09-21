package com.mospolyteh.data.services.personal

import com.mospolytech.domain.services.personal.Order
import com.mospolytech.domain.services.personal.Personal
import com.mospolytech.domain.services.personal.PersonalData
import com.mospolytech.domain.services.personal.Subdivision
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

fun PersonalResponse.User.isStudent(): Boolean {
    return userStatus == "stud"
}

fun PersonalResponse.User.isStaff(): Boolean {
    return userStatus == "staff"
}

fun PersonalResponse.toModel(): Personal {
    val fullName = this.user.surname + " " + this.user.name + " " + this.user.patronymic
    val isStudent = user.isStudent()
    val description =
        if (isStudent) {
            "${user.group} • ${user.degreeLevel.lowercase()}"
        } else {
            "Преподаватель"
        }
    return Personal(
        id = this.user.id.toString(),
        name = fullName,
        description = description,
        avatar = this.user.avatar.ifEmpty { null },
        data =
        buildList {
            add(PersonalData(title = "Статус", value = user.status))
            add(PersonalData(title = "Курс", value = user.course))
            add(PersonalData(title = "Дата рождения", value = user.birthday))
            add(PersonalData(title = "Пол", value = user.sex.getSex()))
            add(PersonalData(title = "Код студента", value = user.code))
            add(PersonalData(title = "Факультет", value = user.faculty))
            add(PersonalData(title = "Группа", value = user.group))
            add(PersonalData(title = "Направление", value = user.specialty))
            user.specialization.ifEmpty { null }?.let { specialization ->
                add(PersonalData(title = "Специализация", value = specialization))
            }
            add(PersonalData(title = "Срок обучения", value = user.degreeLength.filter { it.isDigit() }))
            add(PersonalData(title = "Форма обучения", value = user.educationForm))
            add(PersonalData(title = "Вид финансирования", value = user.finance))
            add(PersonalData(title = "Год набора", value = user.enterYear))
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
