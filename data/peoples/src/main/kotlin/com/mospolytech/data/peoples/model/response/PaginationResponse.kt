package com.mospolytech.data.peoples.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationResponse<T>(
    @SerialName("pages")
    val pages: String,
    @SerialName("per_page")
    val perPage: String,
    @SerialName("current_page")
    val currentPage: String,
    @SerialName("items")
    val items: List<T>,
)
