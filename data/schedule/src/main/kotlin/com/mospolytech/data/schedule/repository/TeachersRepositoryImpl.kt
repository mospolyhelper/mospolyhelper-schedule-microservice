package com.mospolytech.data.schedule.repository

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.peoples.model.db.TeachersDb
import com.mospolytech.data.peoples.model.entity.TeacherSafeEntity
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.schedule.repository.TeachersRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.mapLazy
import java.util.UUID

class TeachersRepositoryImpl : TeachersRepository {
    private val map = mutableMapOf<String, String>()

    override suspend fun findAndGetId(name: String): String {
        return MosPolyDb.transaction {
            var id = map[name]

            if (id == null) {
                val fixedName = name.replace('ё', 'e')

                id = TeacherSafeEntity.find { TeachersDb.name eq name }
                    .mapLazy { it.toModel() }
                    .sortedBy { stuffTypeOrder[it.stuffType] }
                    .firstOrNull()
                    ?.id ?: TeacherSafeEntity.find { TeachersDb.name eq fixedName }
                    .mapLazy { it.toModel() }
                    .sortedBy { stuffTypeOrder[it.stuffType] }
                    .firstOrNull()
                    ?.id
            }

            if (id == null) {
                id = TeacherSafeEntity.new(UUID.randomUUID().toString()) {
                    this.name = name
                }.toModel().id
            }

            map[name] = id

            id
        }
    }

    private val stuffTypeOrder = mapOf(
        "Профессорско-преподавательский состав" to 0,
        "Научный работник" to 1,
        "Научно-технический работник" to 2,
        "Учебно-вспомогательный персонал" to 3,
        "Административно-управленческий персонал" to 4,
        "Инной педагогический работник" to 5,
        "Прочий обслуживающий персонал" to 6,
        "Младший обслуживающий персонал" to 7,
    )

    override suspend fun get(id: String): Teacher? {
        return MosPolyDb.transaction {
            TeacherSafeEntity.findById(id)?.toModel()
        }
    }

    override suspend fun getAll(): List<Teacher> {
        return MosPolyDb.transaction {
            TeacherSafeEntity.all()
                .orderBy(TeachersDb.name to SortOrder.ASC)
                .map { it.toModel() }
        }
    }
}
