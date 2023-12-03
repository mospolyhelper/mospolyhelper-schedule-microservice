package com.mospolytech.features.schedule

import com.mospolytech.domain.schedule.model.scheduleInfo.ScheduleInfoObject
import com.mospolytech.domain.schedule.model.source.ScheduleSourceTypes
import com.mospolytech.features.base.utils.setEnumConverter
import io.ktor.util.converters.*

fun DataConversion.Configuration.scheduleDataConversion() {
    setEnumConverter(ScheduleSourceTypes::values)
    setEnumConverter(ScheduleInfoObject::values)
}
