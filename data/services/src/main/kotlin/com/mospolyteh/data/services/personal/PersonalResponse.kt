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
        val id: String,
        @SerialName("user_status")
        val userStatus: String,
        val name: String,
        val surname: String,
        val patronymic: String,
        val avatar: String,
        val birthday: String,
        val sex: String,
        val code: String,
        val faculty: String,
        val specialty: String,
        val specialization: String,
        val degreeLength: String,
        val enterYear: String,
        val orders: List<String>? = null,
        val subdivisions: List<Subdivision>? = null,
        // Student
        @SerialName("status")
        val status: String? = null,
        @SerialName("group")
        val group: String? = null,
        @SerialName("course")
        val course: String? = null,
        @SerialName("educationForm")
        val educationForm: String? = null,
        @SerialName("finance")
        val finance: String? = null,
//        @SerialName("vacation_start")
//        val vacationStart: String? = null,
//        @SerialName("vacation_end")
//        val vacationEnd: String? = null,
        @SerialName("degreeLevel")
        val degreeLevel: String? = null,
        // Staff
        @SerialName("work_place")
        val workPlace: String? = null,
        @SerialName("email_staff")
        val emailStaff: String? = null,
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
            "${user.group} • ${user.degreeLevel?.lowercase() ?: "студент"}"
        } else {
            if (user.workPlace == null) {
                "Сотрудник"
            } else {
                "Сотрудник • ${user.workPlace}"
            }
        }

    return Personal(
        id = this.user.id,
        name = fullName,
        description = description,
        avatar = this.user.avatar.ifEmpty { null },
        data =
        buildList {
            addData("Статус", user.status)
            addData("Курс", user.course)
            addData("Дата рождения", user.birthday)
            addData("Пол", user.sex.getSex())
            addData("Код студента", user.code)
            addData("Факультет", user.faculty)
            addData("Группа", user.group)
            addData("Направление", user.specialty)
            addData("Специализация", user.specialization.ifEmpty { null })
            addData("Срок обучения", user.degreeLength.filter { it.isDigit() })
            addData("Форма обучения", user.educationForm)
            addData("Вид финансирования", user.finance)
            addData("Год набора", user.enterYear)
        },
    )
}
private fun MutableList<PersonalData>.addData(title: String, value: String?) {
    if (value == null) return

    add(PersonalData(title = title, value = value))
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
