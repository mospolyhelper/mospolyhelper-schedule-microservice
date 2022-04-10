package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.ApiGroup
import com.mospolytech.domain.schedule.model.group.Group
import com.mospolytech.domain.schedule.model.group.GroupInfo
import io.ktor.util.*

object LessonGroupsConverter {
    fun convertGroups(groups: List<ApiGroup>): List<GroupInfo> {
        return groups.map { GroupInfo.create(it.title, "Описание группы") }
    }
}