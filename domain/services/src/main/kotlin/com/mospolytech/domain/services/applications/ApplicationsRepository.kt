package com.mospolytech.domain.services.applications

interface ApplicationsRepository {
    fun getApplications(): List<Application>
}
