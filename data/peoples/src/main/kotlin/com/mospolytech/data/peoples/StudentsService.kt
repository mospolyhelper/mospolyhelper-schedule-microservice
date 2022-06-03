package com.mospolytech.data.peoples

import com.mospolytech.domain.base.model.EducationType
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.toForm
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML

class StudentsService(
    private val client: HttpClient
) {
    companion object {
        private const val BaseUrl = "https://e.mospolytech.ru"
        private const val ApiUrl = "$BaseUrl/old/index.php"

        private const val GetStudents = "$ApiUrl?p=portfolio"
        private const val GetPersonal = "$BaseUrl/#/lk_api.php?getUser="
    }

    @OptIn(ExperimentalXmlUtilApi::class)
    fun getStudents(): List<StudentData> {
        val xml = XML {
            unknownChildHandler = UnknownChildHandler { _, _, _, _, _ -> emptyList() }
        }
        val inputStream = javaClass.getResource("/raw/peoples2.xml")?.openStream()
        checkNotNull(inputStream)
        val inputString = inputStream
            .bufferedReader()
            .let {
                val string = it.readText()
                it.close()
                string
            }
            .replaceBefore("<m:Студент>", "")
            .replaceAfterLast("</m:Студент>", "")
            .replace("\t\t\t\t", "")
            .replace("m:", "")
        val students = "<Состав>[^*]*?</Состав>".toRegex()
            .findAll(inputString)
            .map { xml.decodeFromString<StudentData>(it.value) }
            .filter { it.studentStatus.name == "Является студентом" }
            .toList()
        return students
    }

//    suspend fun getClassmates(token: String): List<Student> {
//        val personal = client.get(GetPersonal) {
//            parameter("token", token)
//        }.body<Map<String, String>>()
//        return personal["group"]?.let {
//            client
//                .get(GetStudents) { parameter("objsearch", it) }
//                .body<String>()
//                .let { parse(it,1).data }
//        } ?: emptyList()
//    }
//
//    suspend fun getStudentsHtml(query: String, page: Int): String {
//        return client.get(GetStudents) {
//            parameter("objsearch", query)
//            parameter("pg", page)
//        }.body()
//    }
//
//    suspend fun getStudents(query: String, page: Int): PagingDTO<Student> {
//        return parse(getStudentsHtml(query, page), page)
//    }
//
//    private fun parse(html: String, page: Int): PagingDTO<Student> {
//        val studentRegex = "<h4 style='font-size:20px;'>[^*]*?<a class=\"close-reveal-modal\">x</a>".toRegex()
//        val pagesRegex = "p=portfolio&pg=\\d{1,3}".toRegex()
//        val pages = pagesRegex.findAll(html)
//            .map { it.value.filter { char -> char.isDigit() }.toInt() }
//            .toSortedSet()
//            .toList()
//        val students = studentRegex.findAll(html)
//            .map { it.value.toStudent() }
//            .toList()
//        return PagingDTO(
//            students.count(),
//            if (page > 1) page - 1 else null,
//            if (pages.last() > page) page + 1 else null,
//            students
//        )
//    }
//
//    private fun String.toStudent(): Student {
//        val tagRegex = "<.*?>".toRegex()
//        val nameRegex = "<h4 style='font-size:20px;'>.+</h4>".toRegex()
//        val groupRegex = "Группа: .*?<br />".toRegex()
//        val directionRegex = "Направление подготовки [(]специальность[)]: .*?<br />".toRegex()
//        val specializationRegex = "Специализация: .*?<br />".toRegex()
//        val courseRegex = "Курс: .*?<br />".toRegex()
//        val formRegex = "Форма обучения: .*?<br />".toRegex()
//        val direction = directionRegex.find(this)?.value?.replace(tagRegex, "")?.replace("Направление подготовки (специальность): ", "").orEmpty()
//        val educationType = when {
//            direction.contains(".02.") -> EducationType.College
//            direction.contains(".03.") -> EducationType.Bachelor
//            direction.contains(".04.") -> EducationType.Magistrate
//            direction.contains(".05.") -> EducationType.Specialist
//            direction.contains(".06.") -> EducationType.Aspirant
//            else -> null
//        }
//        return Student(
//            id = "",
//            name = nameRegex.find(this)?.value?.replace(tagRegex, "").orEmpty(),
//            avatar = "https://e.mospolytech.ru/old/img/no_avatar.jpg",
//            educationForm = formRegex.find(this)?.value?.replace(tagRegex, "")?.replace("Форма обучения: ", "")?.toForm(),
//            course = courseRegex.find(this)?.value?.replace(tagRegex, "")?.replace("Курс: ", "")?.toIntOrNull(),
//            group = groupRegex.find(this)?.value?.replace(tagRegex, "")?.replace("Группа: ", "").orEmpty(),
//            direction = direction,
//            specialization = specializationRegex.find(this)?.value?.replace(tagRegex, "")?.replace("Специализация: ", "").orEmpty(),
//            dialogId = null,
//            additionalInfo = null,
//            educationType = educationType
//        )
//    }
}