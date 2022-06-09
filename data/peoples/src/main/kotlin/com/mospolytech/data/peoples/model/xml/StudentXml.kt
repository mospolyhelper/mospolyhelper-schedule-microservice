package com.mospolytech.data.peoples.model.xml

import com.mospolytech.domain.base.model.EducationType
import com.mospolytech.domain.peoples.model.EducationForm
import com.mospolytech.domain.peoples.model.Student
import kotlinx.datetime.toKotlinLocalDate
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
    val studentFaculty: StudentFaculty,
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
    @SerialName("КодСтудента")
    val studentCode: StudentCode,
    @XmlElement(true)
    @SerialName("СтатусСтудента")
    val studentStatus: StudentStatus,
    @XmlElement(true)
    @SerialName("НомерОбщежитияСтудента")
    val dormitory: Dormitory,
    @XmlElement(true)
    @SerialName("КомнатаСтудента")
    val dormitoryRoom: DormitoryRoom,
    @XmlElement(true)
    @SerialName("ФилиалСтудента")
    val studentBranch: StudentBranch,
)
@kotlinx.serialization.Serializable
@SerialName("Студент")
data class StudentInfo(
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
    val birthday: String
)
@kotlinx.serialization.Serializable
@SerialName("ФакультетСтудента")
data class StudentFaculty(
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
@kotlinx.serialization.Serializable
@SerialName("СпециальностьСтудента")
data class StudentDirection(
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
@kotlinx.serialization.Serializable
@SerialName("СпециализацияСтудента")
data class StudentSpecialization(
    @XmlElement(true)
    @SerialName("Наименование")
    val name: String,
    @XmlElement(true)
    @SerialName("GUIDСпециализацииСтудента")
    val guid: String,
)
@kotlinx.serialization.Serializable
@SerialName("ФормаОбученияСтудента")
data class StudentForm(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)
@kotlinx.serialization.Serializable
@SerialName("ВидФинансированияСтудента")
data class StudentPayment(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)
@kotlinx.serialization.Serializable
@SerialName("УровеньОбразованияСтудента")
data class StudentEducationForm(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)
@kotlinx.serialization.Serializable
@SerialName("ГодНабораСтудента")
data class StudentEducationYear(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)
@kotlinx.serialization.Serializable
@SerialName("НомерКурсаСтудента")
data class StudentEducationCourse(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)
@kotlinx.serialization.Serializable
@SerialName("ГруппаСтудента")
data class StudentEducationGroup(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
    @XmlElement(true)
    @SerialName("GUIDГруппыСтудента")
    val guid: String
)

@kotlinx.serialization.Serializable
@SerialName("КодСтудента")
data class StudentCode(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)

@kotlinx.serialization.Serializable
@SerialName("НомерОбщежитияСтудента")
data class Dormitory(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)

@kotlinx.serialization.Serializable
@SerialName("КомнатаСтудента")
data class DormitoryRoom(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)

@kotlinx.serialization.Serializable
@SerialName("СтатусСтудента")
data class StudentStatus(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)

@kotlinx.serialization.Serializable
@SerialName("ФилиалСтудента")
data class StudentBranch(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
    @XmlElement(true)
    @SerialName("GUIDФилиала")
    val guid: String
)

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun StudentData.toModel(): Student {
    val educationType = when (studentEducationForm.title) {
        "Бакалавриат" -> EducationType.Bachelor
        "Специалитет" -> EducationType.Specialist
        "Аспирантура" -> EducationType.Aspirant
        "Магистратура" -> EducationType.Magistrate
        "СПО" -> EducationType.College
        else -> null
    }
    val educationForm = when (studentForm.title) {
        "Заочная" -> EducationForm.Correspondence
        "Очно-заочная" -> EducationForm.Evening
        "Очная" -> EducationForm.FullTime
        else -> null
    }
    val payment = studentPayment.title
//    val payment = when (studentPayment.title) {
//        "Бюджетная основа" -> EducationForm.Correspondence
//        "Полное возмещение затрат" -> EducationForm.Evening
//        "Целевой прием" -> EducationForm.FullTime
//        else -> null
//    }
    val date = try {
        LocalDate.parse(studentInfo.birthday, dateFormatter).toKotlinLocalDate()
    } catch (e: Throwable) {
        null
    }
    return Student(
        id = studentInfo.id,
        name = studentInfo.lastName + " " + studentInfo.firstName + " " + studentInfo.middleName,
        sex = studentInfo.sex,
        avatar = "https://e.mospolytech.ru/old/img/no_avatar.jpg",
        birthday = date,
        faculty = studentFaculty.titleShort,
        direction = studentDir.title,
        specialization = studentSpec.name.ifEmpty { null },
        educationForm = educationForm,
        educationType = educationType,
        payment = payment,
        course = studentEducationCourse.title.toIntOrNull(),
        group = studentEducationGroup.title.ifEmpty { null },
        years = studentEducationYear.title
    )
}



