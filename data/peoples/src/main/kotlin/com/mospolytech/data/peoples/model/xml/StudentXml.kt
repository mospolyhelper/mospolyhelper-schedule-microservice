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
//    val educationType = when (studentEducationForm.title) {
//        "Бакалавриат" -> EducationType.Bachelor
//        "Специалитет" -> EducationType.Specialist
//        "Аспирантура" -> EducationType.Aspirant
//        "Магистратура" -> EducationType.Magistrate
//        "СПО" -> EducationType.College
//        else -> null
//    }
//    val educationForm = when (studentForm.title) {
//        "Заочная" -> EducationForm.Correspondence
//        "Очно-заочная" -> EducationForm.Evening
//        "Очная" -> EducationForm.FullTime
//        else -> null
//    }
    //val payment = studentPayment.title
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
    val faculty = studentFaculty.toModel()
    val direction = studentDir.toModel()
    val specialization = studentSpec.toModel()
    val group = studentEducationGroup.toModel(studentEducationCourse.title.toIntOrNull(), faculty, direction)
    val branch = studentBranch.toModel()

    return Student(
        id = studentInfo.id,
        firstName = studentInfo.firstName,
        lastName = studentInfo.lastName,
        middleName = studentInfo.middleName,
        sex = studentInfo.sex.ifEmpty { null },
        avatar = "https://e.mospolytech.ru/old/img/no_avatar.jpg",
        birthday = date,
//        faculty = faculty,
//        direction = direction,
        specialization = specialization,
        educationType = studentEducationForm.title,
        educationForm = studentForm.title,
        payment = studentPayment.title,
        course = studentEducationCourse.title.toIntOrNull(),
        group = group,
        years = studentEducationYear.title,
        code = studentCode.title,
        branch = branch,
        dormitory = dormitory.title.ifEmpty { null },
        dormitoryRoom = dormitoryRoom.title.ifEmpty { null }
    )
}

fun StudentFacultyXml.toModel(): StudentFaculty {
    return StudentFaculty(
        id = guid,
        title = title,
        titleShort = titleShort.ifEmpty { null },
    )
}

fun StudentDirectionXml.toModel(): StudentDirection {
    return StudentDirection(
        id = guid,
        title = title,
        code = code
    )
}

fun StudentBranchXml.toModel(): StudentBranch {
    return StudentBranch(
        id = guid,
        title = title
    )
}

fun StudentSpecializationXml.toModel(): StudentSpecialization? {
    return if (title.isEmpty()) {
        null
    } else {
        StudentSpecialization(
            id = guid,
            title = title,
        )
    }
}

fun StudentEducationGroupXml.toModel(course: Int?, faculty: StudentFaculty, direction: StudentDirection): Group? {
    return if (title.isEmpty()) {
        null
    } else {
        Group(
            id = guid,
            title = title,
            course = course,
            faculty = faculty,
            direction = direction
        )
    }
}



