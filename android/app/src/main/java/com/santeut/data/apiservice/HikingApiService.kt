package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.EndHikingRequest
import com.santeut.data.model.request.StartHikingRequest
import com.santeut.data.model.response.StartHikingResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface HikingApiService {

    @POST("/api/party/hiking/start")
    suspend fun startHiking(
        @Body startHikingRequest: StartHikingRequest
    ): CustomResponse<StartHikingResponse>

    @POST("/api/party/hiking/end")
    suspend fun endHiking(
        @Body endHikingRequest: EndHikingRequest
    ): CustomResponse<String>

}