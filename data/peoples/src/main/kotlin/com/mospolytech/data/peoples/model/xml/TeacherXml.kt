package com.mospolytech.data.peoples.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
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
    val status: String,
    @XmlElement(true)
    @SerialName("Должность")
    val post: String,
    @SerialName("Пол")
    @XmlElement(true)
    val sex: String,
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
