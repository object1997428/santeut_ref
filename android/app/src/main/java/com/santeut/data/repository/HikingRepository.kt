package com.santeut.data.repository

import com.santeut.data.model.request.EndHikingRequest
import com.santeut.data.model.request.StartHikingRequest
import com.santeut.data.model.response.StartHikingResponse
import kotlinx.coroutines.flow.Flow

interface HikingRepository {

    suspend fun startHiking(startHikingRequest: StartHikingRequest): StartHikingResponse

    suspend fun endHiking(endHikingRequest: EndHikingRequest): Flow<Unit>


}