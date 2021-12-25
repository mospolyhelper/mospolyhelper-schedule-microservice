package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.ApiGroup
import com.mospolytech.domain.schedule.model.group.Group
import io.ktor.util.*

object LessonGroupsConverter {
    fun convertGroups(groups: List<ApiGroup>): List<Group> {
        return groups.map { Group(it.title.encodeBase64(), it.title) }
    }
}