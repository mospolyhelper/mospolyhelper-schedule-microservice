package com.mospolytech.domain.base.model

import kotlinx.serialization.Serializable

@Serializable
data class PagingDTO<T>(
    val count: Int,
    val previous: String?,
    val next: String?,
    val data: List<T>,
)
