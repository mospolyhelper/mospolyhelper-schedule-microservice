package com.mospolytech.mph.data.schedule.converters

import com.mospolytech.mph.data.schedule.model.ApiGroup
import com.mospolytech.mph.domain.schedule.model.Group
import com.mospolytech.mph.domain.schedule.model.Teacher

object LessonGroupsConverter {
    fun convertGroups(groups: List<ApiGroup>): List<Group> {
        return groups.map { Group(it.title) }
    }
}