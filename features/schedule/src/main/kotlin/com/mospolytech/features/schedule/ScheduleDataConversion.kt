package com.mospolytech.features.schedule

import com.mospolytech.domain.schedule.model.source.ScheduleSources
import com.mospolytech.features.base.utils.setEnumConverter
import io.ktor.util.converters.*

fun DataConversion.Configuration.scheduleDataConversion() {
    setEnumConverter(ScheduleSources::values)
}

