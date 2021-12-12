package com.mospolytech.features.schedule

import com.mospolytech.domain.schedule.model.ScheduleSources
import com.mospolytech.features.base.setEnumConverter
import io.ktor.util.converters.*


fun DataConversion.Configuration.scheduleDataConversion() {
    setEnumConverter(ScheduleSources::values)
}

