package com.mospolytech.domain.schedule.model.rrule

import java.util.*

enum class Weekday(val initials: String) {
    Monday("MO"),
    Tuesday("TU"),
    Wednesday("WE"),
    Thursday("TH"),
    Friday("FR"),
    Saturday("SA"),
    Sunday("SU"),
    ;

    companion object {
        fun fromString(string: String?): Weekday? {
            return if (string.isNullOrEmpty() || string.length < 2) {
                null
            } else {
                try {
                    string.lowercase(Locale.getDefault())
                    string[0].uppercaseChar()
                    valueOf(string)
                } catch (e: Exception) {
                    val dayInitials = string.substring(0, 2)

                    for (value in entries) {
                        if (value.initials.equals(dayInitials, true)) {
                            return value
                        }
                    }
                    null
                }
            }
        }
    }
}
