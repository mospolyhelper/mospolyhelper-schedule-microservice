package com.mospolytech.data.peoples

import com.mospolytech.domain.peoples.model.Teacher
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@kotlinx.serialization.Serializable
data class ДанныеОРаботнике(
    @XmlElement(true)
    val ГУИДФизЛица: String,
    @XmlElement(true)
    val ГУИДСотрудника: String,
    @XmlElement(true)
    val СотрудникНаименование: String,
    @XmlElement(true)
    val ТабельныйНомер: String,
    @XmlElement(true)
    val ДатаРождения: String,
    @XmlElement(true)
    val КатегорияПерсонала: String,
    @XmlElement(true)
    val Состояние: String,
    @XmlElement(true)
    val СтраховойНомерПФР: String,
    @XmlElement(true)
    val ПодразделениеРодитель: String,
    @XmlElement(true)
    val ГУИДПодразделенияРодитель: String,
    @XmlElement(true)
    val Должность: String,
    @XmlElement(true)
    val ВидЗанятости: String,
    @XmlElement(true)
    val Ставка: String,
    @XmlElement(true)
    val Пол: String
)

fun ДанныеОРаботнике.toModel() =
    Teacher(
        id = ГУИДСотрудника,
        name = СотрудникНаименование,
        avatar = "https://e.mospolytech.ru/old/img/no_avatar.jpg",
        dialogId = null,
        birthday = try {
            LocalDate.parse(ДатаРождения, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        } catch (e: Throwable) {
            null
        },
        grade = Должность,
        department = ПодразделениеРодитель,
        sex = Пол,
        additionalInfo = null
    )


