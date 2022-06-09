package com.mospolytech.data.peoples.model.xml

import com.mospolytech.domain.base.model.EducationType
import com.mospolytech.domain.peoples.model.*
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.SerialName
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@kotlinx.serialization.Serializable
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
@kotlinx.serialization.Serializable
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
    val birthday: String
)
@kotlinx.serialization.Serializable
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
@kotlinx.serialization.Serializable
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
@kotlinx.serialization.Serializable
@SerialName("СпециализацияСтудента")
data class StudentSpecializationXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
    @XmlElement(true)
    @SerialName("GUIDСпециализацииСтудента")
    val guid: String,
)
@kotlinx.serialization.Serializable
@SerialName("ФормаОбученияСтудента")
data class StudentFormXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)
@kotlinx.serialization.Serializable
@SerialName("ВидФинансированияСтудента")
data class StudentPaymentXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)
@kotlinx.serialization.Serializable
@SerialName("УровеньОбразованияСтудента")
data class StudentEducationFormXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)
@kotlinx.serialization.Serializable
@SerialName("ГодНабораСтудента")
data class StudentEducationYearXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)
@kotlinx.serialization.Serializable
@SerialName("НомерКурсаСтудента")
data class StudentEducationCourseXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)
@kotlinx.serialization.Serializable
@SerialName("ГруппаСтудента")
data class StudentEducationGroupXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
    @XmlElement(true)
    @SerialName("GUIDГруппыСтудента")
    val guid: String
)

@kotlinx.serialization.Serializable
@SerialName("КодСтудента")
data class StudentCodeXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)

@kotlinx.serialization.Serializable
@SerialName("НомерОбщежитияСтудента")
data class DormitoryXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)

@kotlinx.serialization.Serializable
@SerialName("КомнатаСтудента")
data class DormitoryRoomXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)

@kotlinx.serialization.Serializable
@SerialName("СтатусСтудента")
data class StudentStatusXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String
)

@kotlinx.serialization.Serializable
@SerialName("ФилиалСтудента")
data class StudentBranchXml(
    @XmlElement(true)
    @SerialName("Наименование")
    val title: String,
    @XmlElement(true)
    @SerialName("GUIDФилиала")
    val guid: String
)

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun StudentXml.toModel(): Student {
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
    val faculty = StudentFaculty(
        id = studentFaculty.guid,
        title = studentFaculty.title,
        titleShort = studentFaculty.titleShort,
    )
    val direction = StudentDirection(
        id = studentDir.guid,
        title = studentDir.title,
        code = studentDir.code
    )
    val specialization = studentSpec.let {
        StudentSpecialization(
            id = studentSpec.guid,
            title = studentSpec.title,
        )
    }

    val group = studentEducationGroup.let {
        Group(
            id = studentEducationGroup.guid,
            title = studentEducationGroup.title,
            course = studentEducationCourse.title,
            faculty = faculty,
            direction = direction
        )
    }

    val branch = StudentBranch(
        id = studentBranch.guid,
        title = studentBranch.title
    )

    return Student(
        id = studentInfo.id,
        firstName = studentInfo.firstName,
        lastName = studentInfo.lastName,
        middleName = studentInfo.middleName,
        sex = studentInfo.sex,
        avatar = "https://e.mospolytech.ru/old/img/no_avatar.jpg",
        birthday = date,
        faculty = faculty,
        direction = direction,
        specialization = specialization,
        educationForm = studentForm.title,
        educationType = studentEducationForm.title,
        payment = payment,
        course = studentEducationCourse.title.toIntOrNull(),
        group = group,
        years = studentEducationYear.title,
        branch = branch,
        code = studentCode.title,
        dormitory = dormitory.title,
        dormitoryRoom = dormitoryRoom.title
    )
}



