package com.mospolytech.data.peoples

import com.mospolytech.domain.base.model.PagingDTO
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML

class TeachersService {
    @OptIn(ExperimentalXmlUtilApi::class)
    fun getTeachers(): List<EmployeeInfo> {
        val xml = XML {
            unknownChildHandler = UnknownChildHandler { _, _, _, _, _ -> emptyList() }
        }
        val inputStream = javaClass.getResource("/raw/peoples.xml")?.openStream()
        checkNotNull(inputStream)
        val inputString = inputStream
            .bufferedReader()
            .let {
                val string = it.readText()
                it.close()
                string
            }
            .replaceBefore("<m:ДанныеОРаботнике>", "")
            .replaceAfterLast("</m:ДанныеОРаботнике>", "")
            .replace("\t\t\t\t", "")
            //.replace("""\t<m:Паспорт>[^*]*?</m:ДанныеОРаботнике>""".toRegex(), "</m:ДанныеОРаботнике>")
            .replace("m:", "")

        val teachers = "<ДанныеОРаботнике>[^*]*?</ДанныеОРаботнике>".toRegex()
            .findAll(inputString)
            .map { xml.decodeFromString<EmployeeInfo>(it.value) }
            .filter { it.Состояние == "Работа" }
            .toList()
        return teachers
    }
}