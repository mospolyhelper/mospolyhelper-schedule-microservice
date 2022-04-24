package com.mospolytech.domain.personal.repository

import com.mospolytech.domain.personal.model.Order
import com.mospolytech.domain.personal.model.Personal

interface PersonalRepository {
    suspend fun getPersonalInfo(token: String): Result<Personal>
    fun getOrders(): List<Order>
}