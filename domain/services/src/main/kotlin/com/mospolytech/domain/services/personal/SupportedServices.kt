package com.mospolytech.domain.services.personal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupportedServices(
    @SerialName("services")
    val services: List<Services>,
    @SerialName("people")
    val people: PeopleService?,
) {
    @Serializable
    data class PeopleService(
        @SerialName("screens")
        val screens: List<PeopleScreen>,
    ) {
        @Serializable
        data class PeopleScreen(
            @SerialName("title")
            val title: String,
            @SerialName("icon")
            val icon: String,
            @SerialName("endpointName")
            val endpointName: String,
            @SerialName("queryHint")
            val queryHint: String,
        )
    }

    @Serializable
    enum class Services {
        @SerialName("performance")
        PERFORMANCE,

        @SerialName("payments")
        PAYMENTS,

        @SerialName("people")
        PEOPLE,
    }
}
