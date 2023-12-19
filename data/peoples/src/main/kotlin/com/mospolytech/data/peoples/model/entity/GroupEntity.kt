package com.mospolytech.data.peoples.model.entity

import com.mospolytech.data.peoples.model.db.*
import com.mospolytech.domain.base.utils.ifNotEmpty
import com.mospolytech.domain.peoples.model.Group
import com.mospolytech.domain.peoples.model.GroupShort
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class GroupEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, GroupEntity>(GroupsDb)

    var title by GroupsDb.title
    var course by GroupsDb.course
    var faculty by GroupsDb.faculty
    var direction by GroupsDb.direction

    fun toModel(): Group {
        return Group(
            id = id.value,
            title = title,
            course = course,
            faculty = faculty,
            direction = direction,
        )
    }
}

fun GroupEntity.toShortModel(): GroupShort {
    return GroupShort(
        id = id.value,
        title = title,
        description = description,
    )
}

val GroupEntity.description: String?
    get() =
        buildString {
            course?.let { append("$course-й курс") }

            if (direction != null) {
                direction?.let {
                    ifNotEmpty { append(", ") }
                    append(direction)
                    faculty?.let { faculty ->
                        val shortFaculty = getShortFaculty(faculty)
                        append(shortFaculty)
                    }
                }
            } else {
                faculty?.let { faculty ->
                    ifNotEmpty { append(", ") }
                    append(faculty)
                }
            }
        }.ifEmpty { null }

private fun getShortFaculty(faculty: String): String? {
    return when (faculty) {
        "Полиграфический институт" -> "ПИ"
        "Факультет урбанистики и городского хозяйства" -> "ФУиГХ"
        "Факультет информационных технологий" -> "ФИТ"
        "Факультет машиностроения" -> "ФМ"
        "Факультет химической технологии и биотехнологии" -> "ФХТБ"
        "Транспортный факультет" -> "ТФ"
        "Институт графики и искусства книги имени В.А. Фаворского" -> "ИГиИК"
        "Институт принтмедиа и информационных технологий" -> "ИПИТ"
        "Факультет экономики и управления" -> "ФЭУ"
        "Институт коммуникаций и медиабизнеса" -> "ИКИМ"
        "Институт издательского дела и журналистики" -> "ИДИЖ"
        else -> null
    }
}
