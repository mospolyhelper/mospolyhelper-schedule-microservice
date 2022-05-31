package com.mospolytech.data.peoples

import com.mospolytech.domain.base.model.Department
import com.mospolytech.domain.peoples.model.Teacher
import kotlinx.serialization.SerialName
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@kotlinx.serialization.Serializable
@SerialName("ДанныеОРаботнике")
data class EmployeeInfo(
    @XmlElement(true)
    @SerialName("ГУИДФизЛица")
    val guid: String,
    @XmlElement(true)
    @SerialName("ГУИДСотрудника")
    val guidStaff: String,
    @XmlElement(true)
    @SerialName("СотрудникНаименование")
    val name: String,
    @XmlElement(true)
    @SerialName("ДатаРождения")
    val birthDate: String,
    @XmlElement(true)
    @SerialName("КатегорияПерсонала")
    val stuffType: String,
    @XmlElement(true)
    @SerialName("Состояние")
    val Состояние: String,
    @XmlElement(true)
    @SerialName("СтраховойНомерПФР")
    val СтраховойНомерПФР: String,
    @XmlElement(true)
    @SerialName("Должность")
    val post: String,
    @XmlElement(true)
    @SerialName("ВидЗанятости")
    val ВидЗанятости: String,
    @XmlElement(true)
    @SerialName("Ставка")
    val Ставка: String,
    @SerialName("Пол")
    @XmlElement(true)
    val Sex: String,
    @SerialName("ЭлПочтаСлужебная")
    @XmlElement(true)
    val email: String?,
    @XmlElement(true)
    @SerialName("ПодразделениеРодитель")
    val departmentParent: String,
    @XmlElement(true)
    @SerialName("ГУИДПодразделенияРодитель")
    val departmentParentGuid: String,
    @SerialName("Подразделение")
    @XmlElement(true)
    val department: String?,
    @SerialName("ГУИДПодразделения")
    @XmlElement(true)
    val departmentGuid: String?,
)

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

fun EmployeeInfo.toModel(): Teacher {
    val stuffType = when (stuffType) {
        "ППС" -> "Профессорско-преподавательский состав"
        "АУП" -> "Административно-управленческий персонал"
        "УВП" -> "Учебно-вспомогательный персонал"
        "ПОП" -> "Прочий обслуживающий персонал"
        "МОП" -> "Младший обслуживающий персонал"
        "ИПР" -> "Инной педагогический работник"
        "НТР" -> "Научно-технический работник"
        "НР" -> "Научный работник"
        else -> stuffType
    }

    return Teacher(
        id = guid,
        name = name,
        avatar = "https://e.mospolytech.ru/old/img/no_avatar.jpg",
        dialogId = null,
        stuffType = stuffType,
        birthday = try {
            LocalDate.parse(birthDate, dateFormatter)
        } catch (e: Throwable) {
            null
        },
        grade = post,
        departmentParent = Department(
            id = departmentParentGuid,
            title = departmentParent
        ),
        department = if (department != null && departmentGuid != null) Department(
            id = departmentGuid,
            title = department
        ) else null,
        sex = Sex,
        email = email,
        additionalInfo = null
    )
}



