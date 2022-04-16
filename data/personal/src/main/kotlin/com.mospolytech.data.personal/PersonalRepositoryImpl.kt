package com.mospolytech.data.personal

import com.mospolytech.domain.personal.model.Order
import com.mospolytech.domain.personal.model.Personal
import com.mospolytech.domain.personal.repository.PersonalRepository
import java.time.LocalDate

class PersonalRepositoryImpl(
    private val service: PersonalService
): PersonalRepository {
    override suspend fun getPersonalInfo(token: String): Result<Personal> {
        return runCatching {
            val personalResponse = service.getPersonalInfo(token)
            personalResponse.toModel()
        }
    }

    override fun getOrders(): List<Order> {
        return listOf(Order("1488", LocalDate.now(), "123"), Order("228", LocalDate.now(), "321"))
    }
}