package com.mospolytech.domain.applications.repository

import com.mospolytech.domain.applications.model.Application

interface ApplicationsRepository {
    fun getApplications(): List<Application>
}