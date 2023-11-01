package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.schedule.model.place.PlaceInfo

interface PlacesRepository {
    suspend fun get(id: String): PlaceInfo?
    suspend fun getAll(): List<PlaceInfo>
    suspend fun getPaging(query: String, pageSize: Int, page: Int): PagingDTO<PlaceInfo>
}
