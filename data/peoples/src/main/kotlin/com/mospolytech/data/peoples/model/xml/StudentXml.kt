package com.mospolytech.data.peoples.model.xml

import com.mospolytech.domain.peoples.model.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("Состав")
data class StudentXml(
    @XmlElement(true)
    @SerialName("Студент")
    val studentInfo: StudentInfoXml,
    @XmlElement(true)
    @SerialName("ФакультетСтудента")
    val studentFaculty: StudentFacultyXml,
    @XmlElement(true)
    @SerialName("СпециальностьСтудента")
    val studentDir: StudentDirectionXml,
    @XmlElement(true)
    @SerialName("СпециализацияСтудента")
    val studentSpec: StudentSpecializationXml,
    @XmlElement(true)
    @SerialName("ФормаОбученияСтудента")
    val studentForm: StudentFormXml,
    @XmlElement(true)
    @SerialName("ВидФинансированияСтудента")
    val studentPayment: StudentPaymentXml,
    @XmlElement(true)
    @SerialName("УровеньОбразованияСтудента")
    val studentEducationForm: StudentEducationFormXml,
    @XmlElement(true)
    @SerialName("ГодНабораСтудента")
    val studentEducationYear: StudentEducationYearXml,
    @XmlElement(true)
    @SerialName("НомерКурсаСтудента")
    val studentEducationCourse: StudentEducationCourseXml,
    @XmlElement(true)
    @SerialName("ГруппаСтудента")
    val studentEducationGroup: StudentEducationGroupXml,
    @XmlElement(true)
    @SerialName("КодСтудента")
    val studentCode: StudentCodeXml,
    @XmlElement(true)
    @SerialName("СтатусСтудента")
    val studentStatus: StudentStatusXml,
    @XmlElement(true)
    @SerialName("НомерОбщежитияСтудента")
    val dormitory: DormitoryXml,
    @XmlElement(true)
    @SerialName("КомнатаСтудента")
    val dormitoryRoom: DormitoryRoomXml,
    @XmlElement(true)
    @SerialName("ФилиалСтудента")
    val studentBranch: StudentBranchXml,
)

@Serializable
@SerialName("Студент")
data class StudentInfoXml(
    @XmlElement(true)
    @SerialName("Имя")
    val firstName: String,
    @XmlElement(true)
    @SerialName("Фамилия")
    val lastName: String,
    @XmlElement(true)
    @SerialName("Отчество")
    val middleName: String,
    @XmlElement(true)
    @SerialName("Пол")
    val sex: String,
    @XmlElement(true)
    @SerialName("GUIDСтудента")
    val id: String,
    @XmlElement(true)
    @SerialName("ДатаРождения")
    val birthday: String,
    @XmlElement(true)
    @SerialName("GUIDЗачетки")
    val recordBookId: String,
)

@Serializable
@SerialName("ФакультетСтудента")
data class StudentFacultyXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val titleShort: String,
    @XmlElement(true)
    @SerialName("НаименованиеПолное")
    val title: String,
    @XmlElement(true)
    @SerialName("GUIDФакультетаСтудента")
    val guid: String,
)

@Serializable
@SerialName("СпециальностьСтудента")
data class StudentDirectionXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
    @XmlElement(true)
    @SerialName("КодСпециальности")
    val code: String,
    @XmlElement(true)
    @SerialName("GUIDСпециальностиСтудента")
    val guid: String,
)

@Serializable
@SerialName("СпециализацияСтудента")
data class StudentSpecializationXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
    @XmlElement(true)
    @SerialName("GUIDСпециализацииСтудента")
    val guid: String,
)

@Serializable
@SerialName("ФормаОбученияСтудента")
data class StudentFormXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
)

@Serializable
@SerialName("ВидФинансированияСтудента")
data class StudentPaymentXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
)

@Serializable
@SerialName("УровеньОбразованияСтудента")
data class StudentEducationFormXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
)

@Serializable
@SerialName("ГодНабораСтудента")
data class StudentEducationYearXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
)

@Serializable
@SerialName("НомерКурсаСтудента")
data class StudentEducationCourseXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
)

@Serializable
@SerialName("ГруппаСтудента")
data class StudentEducationGroupXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
    @XmlElement(true)
    @SerialName("GUIDГруппыСтудента")
    val guid: String,
)

@Serializable
@SerialName("КодСтудента")
data class StudentCodeXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
)

@Serializable
@SerialName("НомерОбщежитияСтудента")
data class DormitoryXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
)

@Serializable
@SerialName("КомнатаСтудента")
data class DormitoryRoomXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
)

@Serializable
@SerialName("СтатусСтудента")
data class StudentStatusXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
)

@Serializable
@SerialName("ФилиалСтудента")
data class StudentBranchXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
    @XmlElement(true)
    @SerialName("GUIDФилиала")
    val guid: String,
)
