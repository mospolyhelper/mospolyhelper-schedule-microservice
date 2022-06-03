package com.mospolytech.data.peoples

import com.mospolytech.domain.base.model.EducationType
import com.mospolytech.domain.peoples.model.EducationForm
import com.mospolytech.domain.peoples.model.Student
import kotlinx.serialization.SerialName
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@kotlinx.serialization.Serializable
@SerialName("Состав")
data class StudentData(
    @XmlElement(true)
    @SerialName("Студент")
    val studentInfo: StudentInfo,
    @XmlElement(true)
    @SerialName("ФакультетСтудента")
    val studentFacult: StudentFacult,
    @XmlElement(true)
    @SerialName("СпециальностьСтудента")
    val studentDir: StudentDirection,
    @XmlElement(true)
    @SerialName("СпециализацияСтудента")
    val studentSpec: StudentSpecialization,
    @XmlElement(true)
    @SerialName("ФормаОбученияСтудента")
    val studentForm: StudentForm,
    @XmlElement(true)
    @SerialName("ВидФинансированияСтудента")
    val studentPayment: StudentPayment,
    @XmlElement(true)
    @SerialName("УровеньОбразованияСтудента")
    val studentEducationForm: StudentEducationForm,
    @XmlElement(true)
    @SerialName("ГодНабораСтудента")
    val studentEducationYear: StudentEducationYear,
    @XmlElement(true)
    @SerialName("НомерКурсаСтудента")
    val studentEducationCourse: StudentEducationCourse,
    @XmlElement(true)
    @SerialName("ГруппаСтудента")
    val studentEducationGroup: StudentEducationGroup,
    @XmlElement(true)
    @SerialName("СтатусСтудента")
    val studentStatus: StudentStatus,
)
@kotlinx.serialization.Serializable
@SerialName("Студент")
data class StudentInfo(
    @XmlElement(true)
    @SerialName("Имя")
    val firstName: String,
    @XmlElement(true)
    @SerialName("Фамилия")
    val secondName: String,
    @XmlElement(true)
    @SerialName("Отчество")
    val surname: String,
    @XmlElement(true)
    @SerialName("Пол")
    val sex: String,
    @XmlElement(true)
    @SerialName("GUIDСтудента")
    val id: String,
    @XmlElement(true)
    @SerialName("ДатаРождения")
    val birthday: String
)
@kotlinx.serialization.Serializable
@SerialName("ФакультетСтудента")
data class StudentFacult(
    @XmlElement(true)
    @SerialName("НаименованиеПолное")
    val name: String
)
@kotlinx.serialization.Serializable
@SerialName("СпециальностьСтудента")
data class StudentDirection(
    @XmlElement(true)
    @SerialName("Наименование")
    val name: String,
    @XmlElement(true)
    @SerialName("КодСпециальности")
    val code: String
)
@kotlinx.serialization.Serializable
@SerialName("СпециализацияСтудента")
data class StudentSpecialization(
    @XmlElement(true)
    @SerialName("Наименование")
    val name: String
)
@kotlinx.serialization.Serializable
@SerialName("ФормаОбученияСтудента")
data class StudentForm(
    @XmlElement(true)
    @SerialName("Наименование")
    val name: String
)
@kotlinx.serialization.Serializable
@SerialName("ВидФинансированияСтудента")
data class StudentPayment(
    @XmlElement(true)
    @SerialName("Наименование")
    val name: String
)
@kotlinx.serialization.Serializable
@SerialName("УровеньОбразованияСтудента")
data class StudentEducationForm(
    @XmlElement(true)
    @SerialName("Наименование")
    val name: String
)
@kotlinx.serialization.Serializable
@SerialName("ГодНабораСтудента")
data class StudentEducationYear(
    @XmlElement(true)
    @SerialName("Наименование")
    val name: String
)
@kotlinx.serialization.Serializable
@SerialName("НомерКурсаСтудента")
data class StudentEducationCourse(
    @XmlElement(true)
    @SerialName("Наименование")
    val name: String
)
@kotlinx.serialization.Serializable
@SerialName("ГруппаСтудента")
data class StudentEducationGroup(
    @XmlElement(true)
    @SerialName("Наименование")
    val name: String
)
@kotlinx.serialization.Serializable
@SerialName("СтатусСтудента")
data class StudentStatus(
    @XmlElement(true)
    @SerialName("Наименование")
    val name: String
)

fun StudentData.toModel(): Student {
    val educationType = when (studentEducationForm.name) {
        "Бакалавриат" -> EducationType.Bachelor
        "Специалитет" -> EducationType.Specialist
        "Аспирантура" -> EducationType.Aspirant
        "Магистратура" -> EducationType.Magistrate
        "СПО" -> EducationType.College
        else -> null
    }
    val educationForm = when (studentForm.name) {
        "Заочная" -> EducationForm.Correspondence
        "Очно-заочная" -> EducationForm.Evening
        "Очная" -> EducationForm.FullTime
        else -> null
    }
    val payment = studentPayment.name.contains("Полное возмещение затрат")
    val date = try {
        LocalDate.parse(studentInfo.birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    } catch (e: Throwable) {
        null
    }
    return Student(
        id = studentInfo.id,
        firstName = studentInfo.firstName,
        secondName = studentInfo.secondName,
        surname = studentInfo.surname,
        sex = studentInfo.sex,
        avatar = "https://e.mospolytech.ru/old/img/no_avatar.jpg",
        birthday = date,
        faculty = studentFacult.name,
        direction = studentDir.name,
        specialization = studentSpec.name.ifEmpty { null },
        educationForm = educationForm,
        educationType = educationType,
        payment = payment,
        course = studentEducationCourse.name.toIntOrNull(),
        group = studentEducationGroup.name.ifEmpty { null },
        years = studentEducationYear.name,
        dialogId = null,
        additionalInfo = null,
    )
}



