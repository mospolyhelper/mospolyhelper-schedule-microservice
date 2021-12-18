package com.mospolytech.data.personal

import com.mospolytech.domain.personal.model.EducationType
import com.mospolytech.domain.personal.model.Order
import com.mospolytech.domain.personal.model.Personal
import com.mospolytech.domain.personal.repository.PersonalRepository
import java.time.LocalDate

class PersonalRepositoryImpl: PersonalRepository {
    override fun getPersonalInfo(): Personal {
        return Personal(
            "Иванов И И",
            EducationType.Bachelor,
            "https://sun9-53.userapi.com/impg/8fpYMWRXy_x074ZfuwMIXnL5kigQlW3JYwm8pw/F67JfbPqlOw.jpg?size=960x1280&quality=96&sign=811f1de10e665807c8f576dcb798c483&type=album",
            4,
            "181-721",
            "direction",
            "ipit",
            "228",
            "1488",
            true,
            2020,
            2021,
            LocalDate.now(),
            LocalDate.now()
        )
    }

    override fun getOrders(): List<Order> {
        return listOf(Order("1488", LocalDate.now(), "123"), Order("228", LocalDate.now(), "321"))
    }
}