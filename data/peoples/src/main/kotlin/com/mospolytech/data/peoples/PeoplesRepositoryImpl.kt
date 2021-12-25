package com.mospolytech.data.peoples

import com.mospolytech.domain.base.model.EducationType
import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.base.utils.generators.Generator
import com.mospolytech.domain.peoples.model.EducationForm
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.peoples.repository.PeoplesRepository
import kotlin.random.Random

class PeoplesRepositoryImpl: PeoplesRepository {

    override fun getTeachers(name: String, page: Int, pageSize: Int): PagingDTO<Teacher> {
        return PagingDTO(pageSize, if (page > 1) page - 1 else null, page + 1, List(pageSize)
        {
            Teacher(
                it.toString(),
                "ФИО ${Generator.randomString(10)}",
                "https://sun9-33.userapi.com/impg/Mc88OGc9sf8ROVZfGMWXDgSkFtKJMvBCGmBDbQ/C9txqDUM78s.jpg?size=715x408&quality=96&sign=44a2c1be7fcd36d1d9b9a48f19c51392&type=album",
                Generator.generateStringId(),
                "Старший преподватель",
                "ИПИТ",
                "1234")
        })
    }

    override fun getStudents(name: String, page: Int, pageSize: Int): PagingDTO<Student> {
        return PagingDTO(pageSize, if (page > 1) page - 1 else null, page + 1, List(pageSize)
        {
            Student(
                it.toString(),
                "ФИО ${Generator.randomString(10)}",
                "https://sun9-33.userapi.com/impg/Mc88OGc9sf8ROVZfGMWXDgSkFtKJMvBCGmBDbQ/C9txqDUM78s.jpg?size=715x408&quality=96&sign=44a2c1be7fcd36d1d9b9a48f19c51392&type=album",
                EducationType.values().toList().shuffled().first(),
                Random.nextInt(0, 5),
                "ИПИТ",
                "Ифнормационные системы и технологии",
                "Автомтизация",
                EducationForm.values().toList().shuffled().first(),
                Generator.generateStringId(),
                "1234"
            )
        })
    }

    override fun getClassmates(name: String): List<Student> {
        val list = List(30)
        {
            Student(
                it.toString(),
                "ФИО ${Generator.randomString(10)}",
                "https://sun9-33.userapi.com/impg/Mc88OGc9sf8ROVZfGMWXDgSkFtKJMvBCGmBDbQ/C9txqDUM78s.jpg?size=715x408&quality=96&sign=44a2c1be7fcd36d1d9b9a48f19c51392&type=album",
                null,
                null,
                null,
                null,
                null,
                null,
                Generator.generateStringId(),
                "1234"
            )
        }
        return list.filter { it.name.contains(name, ignoreCase = true) }
    }
}