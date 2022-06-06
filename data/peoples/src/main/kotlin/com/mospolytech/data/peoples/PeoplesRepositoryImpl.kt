package com.mospolytech.data.peoples

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.peoples.repository.PeoplesRepository
import com.mospolytech.domain.personal.repository.PersonalRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PeoplesRepositoryImpl(
    studentsService: StudentsService,
    teachersService: TeachersService,
    private val peoplesDb: PeoplesDb,
    private val personalRepository: PersonalRepository
): PeoplesRepository {

    private val teachersLocalCache by lazy {
        teachersService.getTeachers()
            .asSequence()
            .map { it.toModel() }
            .distinctBy { it.id }
            .toList()
    }

    private val studentsLocalCache by lazy {
        studentsService.getStudents()
            .asSequence()
            .map { it.toModel() }
            .distinctBy { it.id }
            .toList()
    }

    override suspend fun getTeachers(name: String, page: Int, pageSize: Int) =
        peoplesDb.getTeachersPaging(name, pageSize, page)

    override suspend fun getTeachers() = peoplesDb.getTeachers()

    override suspend fun getStudents(name: String, page: Int, pageSize: Int) =
        peoplesDb.getStudentsPaging(name, pageSize, page)

    override suspend fun getStudents() = peoplesDb.getStudents()

    override suspend fun getClassmates(token: String): Result<List<Student>> {
        return personalRepository.getPersonalInfo(token).mapCatching {
            getStudents(it.group, 1, 10000).data
        }
    }

    override suspend fun updateData() {
        peoplesDb.clearData()
        teachersLocalCache.forEach(peoplesDb::addTeacher)
        studentsLocalCache.forEach(peoplesDb::addStudent)
    }

}
