package com.mospolytech.mph.features.schedule

import com.mospolytech.mph.domain.schedule.model.ScheduleSources
import com.mospolytech.mph.features.base.setEnumConverter
import io.ktor.util.converters.*


fun DataConversion.Configuration.scheduleDataConversion() {
    setEnumConverter(ScheduleSources::values)
}

