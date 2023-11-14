package com.mospolytech.domain.schedule.model.rrule

import java.util.*

enum class Frequency {
    Daily,
    Weekly,
    Monthly,
    Yearly,
    ;

    override fun toString(): String {
        return super.toString().uppercase(Locale.getDefault())
    }
}
