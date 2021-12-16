package com.mospolytech.domain.personal.repository

import com.mospolytech.domain.personal.model.Order
import com.mospolytech.domain.personal.model.Personal

interface PersonalRepository {
    fun getPersonalInfo(): Personal
    fun getOrders(): List<Order>
}